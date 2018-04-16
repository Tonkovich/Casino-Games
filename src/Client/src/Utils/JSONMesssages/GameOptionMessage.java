package Utils.JSONMesssages;

import Models.Player;

import javax.json.Json;
import javax.json.JsonObject;

public class GameOptionMessage {

    private Player p = Player.getInstance();

    public String gameOptionsSend() {
        JsonObject json = Json.createObjectBuilder()
                .add("gameOptions", "Requesting game options.")
                .add("userID", p.getUserID()).build();
        return json.toString();
    }

    public String joinGame(int gameID) {
        JsonObject json = Json.createObjectBuilder()
                .add("joinGame", "Join game.")
                .add("gameID", gameID)
                .add("userID", p.getUserID()).build();
        return json.toString();
    }

    public String createGame(int maxPlayers, int bigBlind, int smallBlind) {
        JsonObject json = Json.createObjectBuilder()
                .add("createGame", "Create Game.")
                .add("maxPlayers", maxPlayers)
                .add("bigBlind", bigBlind)
                .add("smallBlind", smallBlind)
                .add("userID", p.getUserID()).build();
        return json.toString();
    }
}
