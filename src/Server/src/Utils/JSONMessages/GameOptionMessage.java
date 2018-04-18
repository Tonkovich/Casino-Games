package Utils.JSONMessages;

import Utils.Database.Games;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Set;

public class GameOptionMessage {

    public String availableGames(Set<Integer> gameIDs) {
        JsonObject json; // Null object
        if (gameIDs.size() != 0) {

            JsonObjectBuilder options = Json.createObjectBuilder();

            int i = 0;
            for (Integer j : gameIDs) {
                options.add("" + i, j);
                i++;
            }
            json = Json.createObjectBuilder()
                    .add("gameOptions", "") // Serve as header
                    .add("size", gameIDs.size())
                    .add("available", options.build()).build();
        } else {
            // No games available, send create message, client will handle interpretation of response
            json = Json.createObjectBuilder()
                    .add("gameOptions", "") // Serve as header
                    .add("createGame", "No available games..\nWant to create one?").build();
        }
        return json.toString();
    }

    public String startGUI(int gameID) {
        int bigBlind = Games.getInstance().getPokerGame(gameID).bigBlind;
        int smallBlind = Games.getInstance().getPokerGame(gameID).smallBlind;
        JsonObject json = Json.createObjectBuilder()
                .add("startGUI", "starting GUI.")
                .add("smallBlind", smallBlind)
                .add("bigBlind", bigBlind).build();
        return json.toString();
    }
}
