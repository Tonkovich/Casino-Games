package Utils.JSONMesssages;

import Utils.ClientSocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;

public class LoginMessages {

    public boolean login(String username, String password, ClientSocket mySocket) {
        JsonObject json = Json.createObjectBuilder()
                .add("login", Json.createObjectBuilder()
                        .add("username", username)
                        .add("password", password)
                        .build()
                ).build();

        boolean response = false;

        try {
            mySocket.sendMessage(json.toString());
            JsonReader jsonReader = Json.createReader(new StringReader(mySocket.receiveMessage()));
            // TODO Timer to disconnect, assume server crashed
            JsonObject obj = jsonReader.readObject();

            String login = obj.getString("login");
            switch (login) {
                case "reject":
                    break;
                case "success":
                    response = true;
                    break;
                default:
                    break;
            }


        } catch (IOException ex) {
            ex.getMessage();
        }

        return response;
    }

}
