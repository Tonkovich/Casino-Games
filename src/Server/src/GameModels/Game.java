package GameModels;

import java.util.HashMap;

public class Game {

    private HashMap<Integer, Player> gamePlayers = new HashMap<>();

    public Player getPlayer(int userID) {
        return gamePlayers.get(userID);
    }

    public void addPlayer(Player newPlayer, int userID) {
        gamePlayers.put(userID, newPlayer);
    }
}
