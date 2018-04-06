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

    public String successLogin(int userID, double wallet) {
        // Send data on login

        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("login", "success")
                .add("userID", userID)
                .add("wallet", wallet).build();
        return json.toString();
    }
}
