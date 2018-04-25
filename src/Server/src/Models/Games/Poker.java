package Models.Games;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Deck;
import Models.Parts.CardGame.Hand;
import Models.Parts.CardGame.Ranking.EvaluateHand;
import Utils.Database.Database;
import Utils.Database.Games;
import Utils.JSONMessages.GameOptionMessage;
import Utils.JSONMessages.PokerMessages;
import Utils.JSONMessages.UserInterfaceMessages;

import java.util.*;

/**
 * This class will hold all data and logic pertaining to the poker game
 */
public class Poker implements CardGame {

    private double pot;
    private Deck deck;
    private Hand house;
    public int bigBlind;
    public PokerThread pt;
    public int smallBlind;
    public int maxSetPlayer;
    private PokerMessages pm;
    private String[] rolePositions;
    public boolean gameDone = false;
    public boolean maxPlayers = false;
    private int currentPosOfSmallBlind;
    private boolean initialRound;
    private boolean initialBettingRound; // Same as initialRound?
    public Database db = Database.getInstance();
    private Map<Integer, Hand> playerHands = Collections.synchronizedMap(new LinkedHashMap<>());
    public Map<Integer, Double> playerBets = Collections.synchronizedMap(new LinkedHashMap<>());
    public Map<Integer, Player> players = Collections.synchronizedMap(new LinkedHashMap<>()); // All linkedHashMap to ensure order on placement
    private UserInterfaceMessages ui = new UserInterfaceMessages();
    private GameOptionMessage gom = new GameOptionMessage();
    private Games gameDB = Games.getInstance();
    private Thread t;
    private int gameID;

    public Poker(int gameID) {
        deck = new Deck(); // Deck is loaded and shuffled
        house = new Hand();
        pm = new PokerMessages();
        initialRound = true;
        initialBettingRound = true;
        t = new Thread(pt = new PokerThread(gameID));
        this.gameID = gameID;
    }

    /*
        Initialize all player info
     */
    public void addPlayer(int userID, Player player) {
        Hand hand = new Hand(); // Create empty hand
        players.put(userID, player);
        playerBets.put(userID, 0.0); // Initialize bets
        setPlayerHand(hand, userID);
        deal(userID);
        massSender(pm.addedToGame(player));
        if (players.size() == maxSetPlayer) {
            maxPlayers = true;
            t.start(); // TODO: Remove will be replaced with checkIfReady method in thread
        }
    }

    /*
       This method removes a player from the HashMap containing the Players in a Poker game.
       Ex: Player decides to leave a Poker game after a round has finished.
    */
    public void removePlayer(int userID) {
        /**
         * If player it's player turn, remove, updateClients, next player, keep pot same, update left player in DB
         */
        playerHands.remove(userID);
        playerBets.remove(userID);
        players.remove(userID);
        if (players.size() == 2) {
            exitGame();
            try {
                t.join();
            } catch (InterruptedException ex) {

            }
        }
    }

    public Player getPlayer(int userID) {
        return players.get(userID);
    }

    /**
     * This method initializes an array of Strings that stores a player's role. The indices in
     * this array represent that player's role (i.e., positions[0] refers to player 1's role).
     * <p>
     * This method is only used for the initial round of a poker game.
     */
    public void initializeRoles() {
        int playersSize = players.size();
        rolePositions = new String[playersSize];
        currentPosOfSmallBlind = 0;
        // Setting first 2 positions as their appropriate roles
        rolePositions[currentPosOfSmallBlind] = "Big Blind";
        rolePositions[currentPosOfSmallBlind + 1] = "Small Blind";

        // Setting remaining positions as having no role, if players still remain
        // Index starts at position after big blind
        int nextPos = currentPosOfSmallBlind + 2;
        for (int i = nextPos; i < playersSize; i++) {
            rolePositions[currentPosOfSmallBlind + i] = "No Role";
        }
    }

    /**
     * This method shifts the positions of the roles by one so that each player's role will be
     * changed.
     * <p>
     * By utilizing the modulo operator, I can effectively wrap around the array, thus
     * treating the array as a circle (like a poker table).
     */
    public void shiftRoles() {
        int playersSize = players.size();
        // Shifting the position of roles in the array
        rolePositions[(currentPosOfSmallBlind + 1) % playersSize] = "Big Blind";
        rolePositions[(currentPosOfSmallBlind + 2) % playersSize] = "Small Blind";

        // Shifting remaining positions which are no role.
        // Index starts at position after big blind's newly shifted position
        int nextPos = (currentPosOfSmallBlind + 3) % playersSize;
        boolean isDone = false;
        while (!isDone) {
            // This conditional helps determine when to stop looping and setting No Role to players,
            //  because the next element after nextPos will be the newly set Small Blind so I don't
            //  want to modify that one since it has just been changed to Dealer.
            if (rolePositions[nextPos].equals("Small Blind")) {
                isDone = true;
            }
            rolePositions[nextPos] = "No Role";
            nextPos = (nextPos + 1) % playersSize;
        }
        // The position of the dealer has been shifted by one.
        currentPosOfSmallBlind = (currentPosOfSmallBlind + 1) % playersSize;
    }

    /*
        This method sets the Player role attribute (dealer, small blind, big blind, noRole*)
    */
    public void setPlayerRoles() {
        if (initialRound) {
            initializeRoles();
            initialRound = false;
        } else { // Else, it's a subsequent round so the roles are shifted.
            shiftRoles();
        }

        int i = 0;
        Iterator<Player> itr = players.values().iterator();
        while (itr.hasNext()) {
            itr.next().setPlayerRole(rolePositions[i]);
            i++;
        }
    }

    public void deal(int userID) {
        for (int i = 0; i < 2; i++) { // Two cards: Texas Hold'em
            Card c = deck.drawCard();
            c.setIsPlayers(true);
            playerHands.get(userID).addCard(c);
        }

        // Update all clients
        updateClients();
    }

    public void drawNextCard() {
        deck.drawCard(); // Burn one card
        house.addCard(deck.drawCard());
        updateClients();
    }

    public double getPot() {
        return pot;
    }

    public void addToPotCall(int userID) {

        Player p = players.get(userID);
        // Find the amount to substract
        double maxBet = 0;
        for (Double j : playerBets.values()) {
            maxBet = j > maxBet ? j : maxBet;
        }

        double currentPlayerBet = playerBets.get(userID);
        double callAmount = maxBet - currentPlayerBet;


        pot += callAmount;
        playerBets.put(userID, playerBets.get(userID) + callAmount);

        if (p.getPlayerWallet() - callAmount == 0) {
            // All in
            massSender(pm.allIn(p));
            p.setAllIn(true);
        } else {
            // Not all in
            massSender(pm.addedToPot(callAmount, pot, players.get(userID)));
        }

        p.setPlayerWallet(p.getPlayerWallet() - callAmount);
        pt.responded = true;
        updateClients();
    }

    public void addToPotRaise(double amount, int userID) {
        Player p = players.get(userID);
        double maxBet = 0;
        for (Double j : playerBets.values()) {
            maxBet = j > maxBet ? j : maxBet;
        }

        double currentPlayerBet = playerBets.get(userID);
        double owe = maxBet - currentPlayerBet;
        double raiseAmount = owe + amount;


        pot += raiseAmount;
        playerBets.put(userID, playerBets.get(userID) + raiseAmount);

        if (p.getPlayerWallet() - raiseAmount == 0) {
            // All in
            massSender(pm.allIn(p));
            p.setAllIn(true);
        } else {
            // Not all in
            massSender(pm.addedToPot(raiseAmount, pot, p));
        }

        p.setPlayerWallet(p.getPlayerWallet() - raiseAmount);
        pt.responded = true;
        updateClients();
    }

    public void addToPotBet(double amount, int userID) {
        Player p = players.get(userID);

        pot += amount;
        playerBets.put(userID, playerBets.get(userID) + amount); // Increase their bet

        if (p.getPlayerWallet() - amount == 0) {
            // All in
            massSender(pm.allIn(p));
            p.setAllIn(true);
        } else {
            // Not all in
            massSender(pm.addedToPot(amount, pot, players.get(userID))); // Send message
        }

        p.setPlayerWallet(p.getPlayerWallet() - amount); // Adjust their wallet
        pt.responded = true;
        updateClients(); // Update all clients
    }

    public void addToPotBlinds(double amount, int userID) {
        Player p = players.get(userID);

        pot += amount;

        if (p.getPlayerWallet() - amount == 0) {
            // All in
            massSender(pm.allIn(p));
            p.setAllIn(true);
        } else {
            // Not all in
            massSender(pm.addedToPot(amount, pot, players.get(userID))); // Send message
        }

        p.setPlayerWallet(p.getPlayerWallet() - amount); // Adjust their wallet
        updateClients(); // Update all clients
    }

    public void check(int userID) {
        pt.responded = true;
        massSender(pm.check(players.get(userID)));
        updateClients();
    }

    public void fold(int userID) {
        players.get(userID).setFolded(true);
        massSender(pm.userFold(players.get(userID).getUsername()));
        pt.responded = true;
        updateClients();
    }

    public Hand getPlayerHand(int userID) {
        return playerHands.get(userID);
    }

    public void setPlayerHand(Hand playerCards, int userID) {
        playerHands.put(userID, playerCards);
    }

    // Final game code
    public void completeGame() {
        getWinner();
        massSender(pm.gameCompleted()); // Ask if they wanna play again
        updateClients(); // Also shows other players cards
    }

    /**
     * Resets the game entirely
     */
    public void startNewGame() {
        gameDone = false;
        //initialRound = true;
        initialBettingRound = true;
        deck = new Deck(); // Deck is loaded and shuffled
        resetPot();
        house = new Hand();
        for (Player p : players.values()) {
            playerHands.put(p.getUserID(), new Hand());
            playerBets.put(p.getUserID(), 0.0);
            deal(p.getUserID());
            p.setFolded(false);
            p.setAllIn(false);
            p.setReady(false);
        }
        pt = new PokerThread(gameID);
        t = new Thread(pt);
        t.start();
        updateClients();
    }

    public void exitGame() {
        massSender(pm.exitGame());
    }

    private void resetPot() {
        pot = 0;
    }

    public void initHouseCard() {
        deck.drawCard();
        deck.drawCard(); // Burn two
        for (int i = 0; i < 3; i++) {
            house.addCard(deck.drawCard());
        }
        initialBettingRound = false;
        updateClients();
    }

    public void getWinner() {
        int rankMax = 0; // set to zero because highcard is zero

        // Key: Player userID, Value: Rank score
        Map<Integer, Integer> ranks = new HashMap<>();

        // Key: highcard score, Value: playerhand key
        Map<Integer, Integer> highcards = new HashMap<>();

        // Handle multiple winner highcards
        List<Integer> winnerHighCards = new ArrayList<>();

        // Highest high card
        int maxHighCard = 0;

        EvaluateHand evaluate;
        // Populate maps and score all players hands
        for (Integer key : playerHands.keySet()) {
            // Skip folded players
            if (!players.get(key).isFolded()) {
                Hand hand = playerHands.get(key);
                evaluate = new EvaluateHand(hand, house);
                ranks.put(players.get(key).getUserID(), evaluate.getRank()); // Put hand rank into map
                highcards.put(players.get(key).getUserID(), evaluate.getHighCard()); // Put highcard into map
            }
        }

        List<Player> winners = new ArrayList<>();

        // Find max hand type
        for (Integer key : ranks.values()) {
            if (key >= rankMax) {
                rankMax = key;
            }
        }

        if (rankMax > 0) {
            for (Integer key : ranks.keySet()) {
                if (ranks.get(key) >= rankMax)
                    winners.add(players.get(key)); // Add winners that have the max rank hand
            }
            // Winners > 1, compare highcards
            if (winners.size() > 1) {

                for (Player p : winners) {
                    // Select all winners highcards, positions in list will correspond with player iteration
                    winnerHighCards.add(highcards.get(p.getUserID()));
                }

                // Find highest highcard
                int duplicates = 0;
                for (int i = 0; i < winnerHighCards.size(); i++) {
                    if (winnerHighCards.get(i) >= maxHighCard) {
                        if (winnerHighCards.get(i) == maxHighCard)
                            duplicates++;
                        maxHighCard = winnerHighCards.get(i);
                    }
                }

                // Players who have same hand and same highcard, divide pot
                if (duplicates > 0) {
                    multipleWinners(duplicates, maxHighCard, highcards);
                } else {
                    // Many players same hand, only one with better high card
                    singleWinner(maxHighCard, rankMax, highcards, ranks);
                }
            } else {
                // Only one winner with best hand
                maxHighCard = highcards.get(winners.get(0).getUserID());
                singleWinner(maxHighCard, rankMax, highcards, ranks);
            }
            // No one has stuff, compare all highcards
        } else {
            // First find highest highcard
            int duplicates = 0;
            for (Integer i : highcards.values()) {
                if (i >= maxHighCard) {
                    if (i == maxHighCard)
                        duplicates++;
                    maxHighCard = i;
                }
            }

            if (duplicates > 0) {
                // Multiple highcard winners
                multipleWinners(duplicates, maxHighCard, highcards);
            } else {
                // Single highcard winner
                singleWinner(maxHighCard, rankMax, highcards, ranks);
            }

        }
    }

    public void winByAllFolded(int userID) {
        Player p = players.get(userID);

        p.setPlayerWallet(p.getPlayerWallet() + getPot());
        db.updateWallet(userID, p.getPlayerWallet() + getPot());
        updateAllNonWinnersWallet();
        p.sendMessage(pm.winnerMessage());
        massSender(pm.winnerMessageOthers(p));
        massSender(pm.gameCompleted()); // Ask if they wanna play again
        updateClients(); // Also shows other players cards
    }

    /**
     * Single winner is handled here
     *
     * @param maxHighCard largest highcard in game
     * @param rankMax     largest rank
     * @param highcards   all player highcards
     * @param ranks       all ranking of playerHands
     */
    private void singleWinner(int maxHighCard, int rankMax, Map<Integer, Integer> highcards, Map<Integer, Integer> ranks) {

        //System.out.println("Single");

        for (Integer i : highcards.keySet()) {
            if (highcards.get(i) == maxHighCard && ranks.get(i) == rankMax) {
                Player player = players.get(i);

                //System.out.println("Player hand: " + playerHands.get(player.getUserID()).toString());
                //System.out.println(player.getUsername());


                player.setPlayerWallet(player.getPlayerWallet() + getPot());
                db.updateWallet(i, player.getPlayerWallet() + getPot());
                players.get(i).sendMessage(pm.winnerMessage());
                massSender(pm.winnerMessageOthers(players.get(i)));
            }
        }
        updateAllNonWinnersWallet();
    }

    /**
     * Multiple winners are handled here
     *
     * @param duplicates  how many winners
     * @param maxHighCard largest highcard
     * @param highcards   hashmap of all highcards
     */
    private void multipleWinners(int duplicates, int maxHighCard, Map<Integer, Integer> highcards) {
        double divPot = getPot() / duplicates + 1; // Split pot between players

        //System.out.println("Multiple");

        List<Player> finalWinners = new ArrayList<>();
        for (Integer i : highcards.keySet()) {
            if (highcards.get(i) == maxHighCard) {
                Player player = players.get(i);
                finalWinners.add(players.get(i));

                //System.out.println(player.getUsername());


                player.sendMessage(pm.winnerMessage()); // Send each player winner message
                player.setPlayerWallet(player.getPlayerWallet() + divPot); // Give them their winnings
                db.updateWallet(i, player.getPlayerWallet() + divPot);
            }
        }
        updateAllNonWinnersWallet();
        massSender(pm.multipleWinners(finalWinners));
    }

    /**
     * Send messages to all clients
     *
     * @param message incoming json
     */
    private void massSender(String message) {
        for (Player p : players.values()) {
            p.sendMessage(message);
        }
    }

    private void updateAllNonWinnersWallet() {
        for (Player p : players.values()) {
            db.updateWallet(p.getUserID(), p.getPlayerWallet());
        }
    }

    /**
     * Sends graphical updates to clients
     */
    public void updateClients() {
        for (Integer i : playerHands.keySet()) {
            // players.size() - 1, subtract user being updated
            String message = ui.updateClients(pot, playerHands.get(i), house, initialBettingRound
                    , smallBlind, bigBlind, playerBets.values(), players.values()
                    , 0.0, players.keySet(), gameDone, playerHands);
            players.get(i).sendMessage(message);
        }
    }

    /**
     * Will send user requests to either (fold, check, or raise) or (fold, call, or raise)
     * based off message used. See PokerMessages pokerAction type messages.
     * <p>
     * i.e Other user has bet, send call type. Or no one bet, send check type
     *
     * @param message pokerAction message
     * @param userID  user receiving message
     */
    public void askPlayerForInput(String message, int userID) {
        players.get(userID).sendMessage(message);
    }

}
