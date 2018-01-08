package GameModels;

import java.util.ArrayList;
import java.util.HashMap;

public class Poker {
    /**
     * Will hold Hashmaps and other game data templates
     */
    private double pot;
    private HashMap<Integer, ArrayList<String>> playerHands = new HashMap<>();
    private HashMap<Integer, Player> gamePlayers = new HashMap<>();
    public ArrayList<String> houseCards = new ArrayList<>();
    public ArrayList<String> houseDeck = new ArrayList<>();

    public double getPot() {
        return pot;
    }

    public void addToPot(double amount) {
        pot =+ amount;
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

}
