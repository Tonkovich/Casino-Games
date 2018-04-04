package Utils.JSONMessages;

import javax.json.Json;
import javax.json.JsonObject;

public class HeartbeatMessages {

    public String heartBeatSend() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("heartBeat", "Are you alive?").build();
        return json.toString();
    }
}
