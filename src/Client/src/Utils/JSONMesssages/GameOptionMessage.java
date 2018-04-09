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
}
