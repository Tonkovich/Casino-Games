package Parsers;

import Utils.Database.Games;
import Utils.Database.Players;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;

public class PokerParse {

    private static PokerParse instance;
    private Games games = Games.getInstance();
    private Players players = Players.getInstance();
    private static final Logger log = LogManager.getLogger(PokerParse.class);

    private PokerParse() {
    }

    public static PokerParse getInstance() {
        if (instance == null) {
            instance = new PokerParse();
        }
        return instance;
    }

    /**
     * Will parse client input
     *
     * @param json incoming user message
     */
    public void parse(JsonObject json) {
        int gameID = json.getInt("gameID");
        int userID = json.getInt("userID");
        if (json.getJsonString("call") != null) {
            games.getPokerGame(gameID).addToPotCall(userID);
        } else if (json.getJsonString("check") != null) {
            games.getPokerGame(gameID).check(userID);
        } else if (json.getJsonNumber("raise") != null) {
            double amount = json.getJsonNumber("raise").doubleValue();
            games.getPokerGame(gameID).addToPotRaise(amount, userID);
        } else if (json.getJsonString("fold") != null) {
            games.getPokerGame(gameID).fold(userID);
        } else if (json.getJsonNumber("bet") != null) {
            double amount = json.getJsonNumber("bet").doubleValue();
            games.getPokerGame(gameID).addToPotBet(amount, userID);
        } else if (json.getBoolean("readyUp") || !json.getBoolean("readyUp")) {
            boolean answer = json.getBoolean("readyUp");
            if (answer) {
                players.getPlayer(userID).setReady(json.getBoolean("readyUp"));
            } else {
                games.getPokerGame(gameID).removePlayer(userID);
            }
        }
    }
}
