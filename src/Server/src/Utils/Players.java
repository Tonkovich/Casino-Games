package Utils;

import Models.Games.Player;

import java.util.HashMap;

public class Players {


    private static Players instance;

    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
        }
        return instance;
    }

    private Players() {
    }

    private HashMap<Integer, Player> players = new HashMap<>();

    public Player getPlayer(int userID) {
        return players.get(userID);
    }

    public void loginPlayer(int userID, Player player) {
        players.put(userID, player);
    }

}
