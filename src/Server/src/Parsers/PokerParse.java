package Parsers;

import Utils.Database.Games;

import javax.json.JsonObject;

public class PokerParse {

    private static PokerParse instance;
    private Games games = Games.getInstance();

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
        JsonObject jo = json.getJsonObject("action");
        if (jo.getJsonObject("bet") != null) {
            JsonObject jo1 = jo.getJsonObject("bet");
            double amount = jo1.getJsonNumber("amount").doubleValue();
            games.getPokerGame(gameID).addToPot(amount);
        } else if (jo.getJsonObject("raise") != null) {
            JsonObject jo1 = jo.getJsonObject("raise");
            double amount = jo1.getJsonNumber("amount").doubleValue();
            if (games.getPokerGame(gameID).getPrevBet() != 0) {
                double prevBet = games.getPokerGame(gameID).getPrevBet();
                games.getPokerGame(gameID).addToPot(prevBet + amount);
            }
        } else if (jo.getJsonObject("fold") != null) {
            games.getPokerGame(gameID).fold(userID);
        }

    }
}
