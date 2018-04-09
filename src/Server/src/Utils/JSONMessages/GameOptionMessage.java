package Utils.JSONMessages;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Set;

public class GameOptionMessage {

    public String availableGames(Set<Integer> gameIDs) {
        JsonObject json; // Null object
        if (gameIDs.size() != 0) {

            JsonObjectBuilder options = Json.createObjectBuilder();
            for (Integer i : gameIDs) {
                options.add("available" + i, i);
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
}
