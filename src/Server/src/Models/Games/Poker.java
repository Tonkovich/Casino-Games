package Models.Games;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Deck;
import Models.Parts.CardGame.Hand;
import Models.Parts.CardGame.RankChecks.ScoreHand;
import Utils.JSONMessages.PokerMessages;

import java.util.*;

/**
 * This class will hold all data and logic pertaining to the poker game
 */
public class Poker implements CardGame {

    private double pot;
    private double prevBet = 0;
    private boolean gameReady = false;
    private HashMap<Integer, Hand> playerHands = new HashMap<>();
    private HashMap<Integer, Player> players = new HashMap<>();
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
        start(); // Start thread
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

    public Player getPlayer(int userID) {
        return players.get(userID);
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
        // TODO: Start game here!!!
        massSender(pm.gameStarted());
    }

    public void drawNextCard() {
        deck.drawCard(); // Burn one card
        massSender(pm.cardDrawn(deck.peek())); // Notify all
        house.addCard(deck.drawCard());
    }

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot += amount;
        massSender(pm.addedToPot(amount, pot, turns.peek()));
        prevBet = amount;
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

        ScoreHand scoreHand;
        // Populate maps and score all players hands
        for (Integer key : playerHands.keySet()) {
            Hand hand = playerHands.get(key);
            scoreHand = new ScoreHand(hand, house);
            ranks.put(players.get(key).getUserID(), scoreHand.getRank()); // Put hand rank into map
            highcards.put(players.get(key).getUserID(), scoreHand.getHighCard()); // Put highcard into map
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

    // Handling a single winner
    private void singleWinner(int maxHighCard, int rankMax, Map<Integer, Integer> highcards, Map<Integer, Integer> ranks) {

        System.out.println("Single");

        for (Integer i : highcards.keySet()) {
            if (highcards.get(i) == maxHighCard && ranks.get(i) == rankMax) {
                Player player = players.get(i);

                System.out.println("Player hand: " + playerHands.get(player.getUserID()).toString());
                System.out.println(player.getUsername());


                player.setPlayerWallet(player.getPlayerWallet() + getPot());
                players.get(i).sendMessage(pm.winnerMessage());
                massSender(pm.winnerMessageOthers(players.get(i)));
            }
        }
    }

    // Handling multiple winners
    private void multipleWinners(int duplicates, int maxHighCard, Map<Integer, Integer> highcards) {
        double divPot = getPot() / duplicates + 1; // Split pot between players

        System.out.println("Multiple");

        List<Player> finalWinners = new ArrayList<>();
        for (Integer i : highcards.keySet()) {
            if (highcards.get(i) == maxHighCard) {
                Player player = players.get(i);
                finalWinners.add(players.get(i));

                System.out.println(player.getUsername());


                player.sendMessage(pm.winnerMessage()); // Send each player winner message
                player.setPlayerWallet(player.getPlayerWallet() + divPot); // Give them their winnings
            }
        }

        massSender(pm.multipleWinners(finalWinners));
    }

    private void massSender(String message) {
        for (Player p : players.values()) {
            p.sendMessage(message);
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
