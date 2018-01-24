package GameModels;

import java.util.ArrayList;
import java.util.HashMap;

public class CardGame extends Game {

    private double pot;
    private HashMap<Integer, ArrayList<String>> playerHands = new HashMap<>();
    private ArrayList<String> houseCards = new ArrayList<>();
    private ArrayList<String> houseDeck = new ArrayList<>();

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot += amount;
    }

    public void resetPot() {
        pot = 0;
    }

    public ArrayList<String> getPlayerHand(int userID) {
        return playerHands.get(userID);
    }

    public void setPlayerHand(ArrayList<String> playerCards, int userID) {
        playerHands.put(userID, playerCards);
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
