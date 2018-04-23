package Utils.JSONMesssages;

import javax.json.Json;
import javax.json.JsonObject;

public class PokerActionMessage {

    public String fold(int gameID, int userID) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("fold", "fold")
                .build();
        return json.toString();
    }

    public String check(int gameID, int userID) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("check", "check")
                .build();
        return json.toString();
    }

    public String call(int gameID, int userID) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("call", "")
                .build();
        return json.toString();
    }

    public String raise(int gameID, int userID, double amount) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("raise", amount)
                .build();
        return json.toString();
    }

    public String readyUp(int gameID, int userID, boolean yes) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("readyUp", yes)
                .build();
        return json.toString();
    }

    public String bet(int gameID, int userID, double amount) {
        JsonObject json = Json.createObjectBuilder()
                .add("gameID", gameID)
                .add("userID", userID)
                .add("gameAction", "Acting upon game")
                .add("gameType", "Poker")
                .add("bet", amount)
                .build();
        return json.toString();
    }

}
