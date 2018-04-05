package Utils;

import Models.Games.Player;

import java.util.Collection;
import java.util.HashMap;

public class Players {


    private static Players instance;
    private HashMap<Integer, Player> players = new HashMap<>();
    private int playerCount = players.size();
    private static HeartBeat hb;

    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
            hb = HeartBeat.getInstance();
        }
        return instance;
    }

    private Players() {

    }

    public Player getPlayer(int userID) {
        return players.get(userID);
    }

    public void loginPlayer(int userID, Player player) {
        players.put(userID, player);
        hb.start(); // Wont do anything if already running. See start()
    }

    public void logoutPlayer(int userID) {
        players.remove(userID);
        if (players.size() == 0) {
            hb.stop(); // Stop heartbeat
        }
    }

    public Collection getPlayers() {
        return players.values();
    }
}
