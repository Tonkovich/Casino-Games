package Parsers;

import Models.Games.Player;
import Utils.Database.Games;
import Utils.Database.Players;
import Utils.JSONMessages.GameOptionMessage;

import javax.json.JsonObject;

public class GameOptionsParse {

    private static GameOptionsParse instance;
    private Games games = Games.getInstance();
    private Players players = Players.getInstance();
    private GameOptionMessage gom = new GameOptionMessage();

    private GameOptionsParse() {
    }

    public static GameOptionsParse getInstance() {
        if (instance == null) {
            instance = new GameOptionsParse();
        }
        return instance;
    }

    public void parse(JsonObject obj) {
        Player p = players.getPlayer(obj.getInt("userID")); // Load player
        p.sendMessage(gom.availableGames(games.getPokerGames())); // Only doing poker now
    }
}
