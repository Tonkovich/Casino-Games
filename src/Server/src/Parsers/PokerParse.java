package Parsers;

import Utils.Database.Games;
import Utils.Database.Players;

import javax.json.JsonObject;

public class PokerParse {

    private static PokerParse instance;
    private Games games = Games.getInstance();
    private Players players = Players.getInstance();

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
            // TODO Possibly move responded on success of add to pot, or isAllowed()
        } else if (json.getJsonString("check") != null) {
            games.getPokerGame(gameID).check();
        } else if (json.getJsonNumber("raise") != null) {
            double amount = json.getJsonNumber("raise").doubleValue();
            games.getPokerGame(gameID).addToPotRaise(amount, userID);
        } else if (json.getJsonString("fold") != null) {
            games.getPokerGame(gameID).fold(userID);
        } else if (json.getJsonNumber("bet") != null) {
            double amount = json.getJsonNumber("bet").doubleValue();
            games.getPokerGame(gameID).addToPotBet(amount, userID);
        } else if (json.getBoolean("readyUp")) {
            players.getPlayer(userID).setReady(json.getBoolean("readyUp"));
        }
    }
}
