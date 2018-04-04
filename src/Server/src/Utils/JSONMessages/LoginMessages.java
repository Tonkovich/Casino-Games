package Utils.JSONMessages;

import javax.json.Json;
import javax.json.JsonObject;

public class LoginMessages {

    public String incorrectLogin() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("reject", "Username or password incorrect!").build();
        return json.toString();
    }

    public String successLogin() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("login", "Success!").build();
        return json.toString();
    }
}
