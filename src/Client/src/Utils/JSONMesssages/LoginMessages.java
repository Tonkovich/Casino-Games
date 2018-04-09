package Utils.JSONMesssages;

import javax.json.Json;
import javax.json.JsonObject;

public class LoginMessages {

    public String login(String username, String password, int heartBeatPort) {
        System.out.println("heartport: " + heartBeatPort);
        JsonObject json = Json.createObjectBuilder()
                .add("login", Json.createObjectBuilder()
                        .add("username", username)
                        .add("password", password)
                        .add("heartBeatPort", heartBeatPort)
                        .build()
                ).build();

        return json.toString();
    }

}
