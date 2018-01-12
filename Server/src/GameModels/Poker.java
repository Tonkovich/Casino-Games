package GameModels;

import java.util.ArrayList;
import java.util.HashMap;

public class Poker extends CardGame{
    /**
     * Will hold Hashmaps and other game data templates
     */
    private double pot;
    private HashMap<Integer, ArrayList<String>> playerHands = new HashMap<>();
    private HashMap<Integer, Player> gamePlayers = new HashMap<>();
    private ArrayList<String> houseCards = new ArrayList<>();
    private ArrayList<String> houseDeck = new ArrayList<>();

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot += amount;
    }

    public void resetPot(){
        pot = 0;
    }

    public ArrayList<String> getPlayerHand(int userID) {
        return playerHands.get(userID);
    }

    public void setPlayerHand(ArrayList<String> playerCards, int userID) {
        playerHands.put(userID, playerCards);
    }

    // Will also be used to update player since "put" also updates
    public void addPlayer(Player newPlayer, int userID) {
        gamePlayers.put(userID, newPlayer);
    }

    public Player getPlayer(int userID) {
        return gamePlayers.get(userID);
    }

    public ArrayList<String> getHouseCards() {
        return houseCards;
    }

    public void setHouseCards(ArrayList<String> houseCards) {
        this.houseCards = houseCards;
    }

    public ArrayList<String> getHouseDeck() {
        return houseDeck;
    }

    public void setHouseDeck(ArrayList<String> houseDeck) {
        this.houseDeck = houseDeck;
    }

}
