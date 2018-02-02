package Models.Game;

import Models.Parts.CardGame.Deck;
import Models.Parts.CardGame.Hand;
import Models.Parts.CardGame.RankChecks.ScoreHand;

import java.util.*;

/**
 * This class will hold all data and logic pertaining to the poker game
 */
public class Poker implements CardGame {

    private double pot;
    private boolean gameReady = false;
    private HashMap<Integer, Hand> playerHands = new HashMap<>();
    private HashMap<Integer, Player> players = new HashMap<>();
    private Hand house;
    private Queue<Player> turns;
    private Deck deck;
    private Timer timer;

    public Poker() {
        deck = new Deck(); // Deck is loaded and shuffled
        turns = new PriorityQueue<>(); // Will keep track of player turns
        house = new Hand();
    }

    public void addPlayer(int userID, Player player) {
        players.put(userID, player);
        turns.add(player);
    }

    public Player getPlayer(int userID) {
        return players.get(userID);
    }

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot += amount;
    }

    public void resetPot() {
        pot = 0;
    }

    public Hand getPlayerHand(int userID) {
        return playerHands.get(userID);
    }

    public void setPlayerHand(Hand playerCards, int userID) {
        playerHands.put(userID, playerCards);
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck newDeck) {
        deck = newDeck;
    }

    public void deal() {
        for (Hand h : playerHands.values()) {
            h.addCard(deck.drawCard());
            h.addCard(deck.drawCard()); // Two cards: Texas Hold'em
        }
    }

    public void initHouseCard() {
        for (int i = 0; i < 3; i++)
            house.addCard(deck.drawCard());
    }

    public void nextHouseCard() {
        deck.drawCard(); // Burn one card
        house.addCard(deck.drawCard());
    }

    public Player getWinner() {
        ScoreHand scoreHand;
        // Using keys because we'll eventually need the key for player association and message sending
        for (Integer key : playerHands.keySet()) {
            Hand hand = playerHands.get(key);
            scoreHand = new ScoreHand(hand, house);
        }
        // TODO: Check all player hands and see who has best
        return null;
    }

    public boolean isMoveAllowed(Player player) {
        return (player.getUserID() == turns.peek().getUserID());
    }

    public void setGameReady() {
        gameReady = true;
    }

    public boolean isGameReady() {
        return gameReady;
    }

    // Final game code
    public void completeRound() {

    }
}
