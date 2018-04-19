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
        if (json.getJsonNumber("call") != null) {
            double amount = json.getJsonNumber("call").doubleValue();
            games.getPokerGame(gameID).addToPot(amount, userID);
        } else if (json.getJsonString("check") != null) {
            // TODO: Just have respond, implement this
        } else if (json.getJsonNumber("raise") != null) {
            double amount = json.getJsonNumber("raise").doubleValue();

            if (games.getPokerGame(gameID).getPrevBet() != 0) {
                double prevBet = games.getPokerGame(gameID).getPrevBet();
                games.getPokerGame(gameID).addToPot(prevBet + amount, userID);
            } else {
                // First bet
                games.getPokerGame(gameID).addToPot(amount, userID);
            }
        } else if (json.getJsonString("fold") != null) {
            games.getPokerGame(gameID).fold(userID);
        }


    }
}
