package Utils.JSONMessages;

import javax.json.Json;
import javax.json.JsonObject;

public class LoginMessages {

    public String incorrectLogin() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("login", "reject").build();
        return json.toString();
    }

    public String successLogin() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("login", "success").build();
        return json.toString();
    }
}
