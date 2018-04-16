package Models.Games;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Deck;
import Models.Parts.CardGame.Hand;
import Models.Parts.CardGame.Ranking.EvaluateHand;
import Utils.JSONMessages.PokerMessages;
import Utils.JSONMessages.UserInterfaceMessages;

import java.util.*;

/**
 * This class will hold all data and logic pertaining to the poker game
 */
public class Poker implements CardGame {

    private double pot;
    private double prevBet = 0;
    private boolean gameReady = false;
    private boolean initialRound;
    private String[] rolePositions;
    private int currentPosOfDealer;
    private HashMap<Integer, Hand> playerHands = new HashMap<>();
    public HashMap<Integer, Player> players = new LinkedHashMap<>();
    private UserInterfaceMessages ui = new UserInterfaceMessages();
    private Hand house;
    private PriorityQueue<Player> turns;
    private Deck deck;
    private Timer timer;
    private PokerMessages pm;

    public Poker() {
        deck = new Deck(); // Deck is loaded and shuffled
        turns = new PriorityQueue<>(); // Will keep track of player turns
        house = new Hand();
        pm = new PokerMessages();
        initialRound = true;
        //start(); // Start thread
    }

    /*
        Initialize all player info
     */
    public void addPlayer(int userID, Player player) {
        // TODO: Make max number of players 6
        Hand hand = new Hand(); // Create empty hand
        players.put(userID, player);
        setPlayerHand(hand, userID);
        turns.add(player);
        massSender(pm.addedToGame(player));
    }

    /*
       This method removes a player from the HashMap containing the Players in a Poker game.
       Ex: Player decides to leave a Poker game after a round has finished.
    */
    public void removePlayer(int userID) {

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
        currentPosOfDealer = 0;
        // Setting first 3 positions as their appropriate roles
        rolePositions[currentPosOfDealer] = "Dealer";
        rolePositions[currentPosOfDealer + 1] = "Small Blind";
        rolePositions[currentPosOfDealer + 2] = "Big Blind";

        // Setting remaining positions as having no role, if players still remain
        // Index starts at position after big blind
        int nextPos = currentPosOfDealer + 3;
        for (int i = nextPos; i < playersSize; i++) {
            rolePositions[currentPosOfDealer + i] = "No Role";
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
        rolePositions[(currentPosOfDealer + 1) % playersSize] = "Dealer";
        rolePositions[(currentPosOfDealer + 2) % playersSize] = "Small Blind";
        rolePositions[(currentPosOfDealer + 3) % playersSize] = "Big Blind";

        // Shifting remaining positions which are no role.
        // Index starts at position after big blind's newly shifted position
        int nextPos = (currentPosOfDealer + 4) % playersSize;
        boolean isDone = false;
        while (!isDone) {
            // This conditional helps determine when to stop looping and setting No Role to players,
            //  because the next element after nextPos will be the newly set Dealer so I don't want
            //  to modify that one since it has just been changed to Dealer.
            if (rolePositions[nextPos].equals("Dealer")) {
                isDone = true;
            }
            rolePositions[nextPos] = "No Role";
            nextPos = (nextPos + 1) % playersSize;
        }
        // The position of the dealer has been shifted by one.
        currentPosOfDealer = (currentPosOfDealer + 1) % playersSize;
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

    public void deal() {
        initHouseCard(); // Start house cards
        for (Hand h : playerHands.values()) {
            for (int i = 0; i < 2; i++) { // Two cards: Texas Hold'em
                Card c = deck.drawCard();
                c.setIsPlayers(true);
                h.addCard(c);
            }
        }
        // Update all clients
        updateClients();

        // TODO: Start game here!!!
        massSender(pm.gameStarted());
    }

    public void drawNextCard() {
        deck.drawCard(); // Burn one card
        massSender(pm.cardDrawn(deck.peek())); // Notify all
        house.addCard(deck.drawCard());
        updateClients();
    }

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot += amount;
        massSender(pm.addedToPot(amount, pot, turns.peek()));
        prevBet = amount;
        updateClients();
    }

    public double getPrevBet() {
        return prevBet;
    }

    public void fold(int userID) {
        // TODO: Remove players hand and etc
        turns.remove();
        massSender(pm.userFold(players.get(userID).getUsername()));
    }

    public boolean isMoveAllowed(Player player) {
        return (player.getUserID() == turns.peek().getUserID());
    }

    public void setGameReady() {
        gameReady = true;
        massSender(pm.gameReady());
    }

    public boolean isGameReady() {
        return gameReady;
    }

    public Hand getPlayerHand(int userID) {
        return playerHands.get(userID);
    }

    public void setPlayerHand(Hand playerCards, int userID) {
        playerHands.put(userID, playerCards);
    }

    // Final game code
    public void completeRound() {
        getWinner();
        deck.clearDeck();
        resetPot();

        // Round over, play again?
        massSender(pm.gameCompleted());
    }


    //// PRIVATE METHODS ////


    private void resetPot() {
        pot = 0;
    }

    private void initHouseCard() {
        for (int i = 0; i < 3; i++)
            house.addCard(deck.drawCard());
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
            Hand hand = playerHands.get(key);
            evaluate = new EvaluateHand(hand, house);
            ranks.put(players.get(key).getUserID(), evaluate.getRank()); // Put hand rank into map
            highcards.put(players.get(key).getUserID(), evaluate.getHighCard()); // Put highcard into map
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
                players.get(i).sendMessage(pm.winnerMessage());
                massSender(pm.winnerMessageOthers(players.get(i)));
            }
        }
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
            }
        }

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

    /**
     * Sends graphical updates to clients
     */
    private void updateClients() {
        for (Integer i : playerHands.keySet()) {
            String message = ui.updateClients(pot, playerHands.get(i), house);
            players.get(i).sendMessage(message);
        }
    }


    //// IMPLEMENTATION CODE /////


    public void start() {
        boolean cont = true;
        Thread t = new Thread(() -> {
            while (cont) {
                /**
                 * GAME INSTANCE LOGIC HERE
                 */
            }
        });
        t.start();
    }
}
