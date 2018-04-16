package Utils.JSONMesssages;

import javax.json.Json;
import javax.json.JsonObject;

public class HeartBeatMessage {
    public String heartBeatSend(int userID) {
        JsonObject json = Json.createObjectBuilder()
                .add("heartBeat", "Yes")
                .add("userID", userID).build();
        return json.toString();
    }
}
