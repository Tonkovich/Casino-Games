package Graphics;

import Utils.ClientSocket;
import Utils.JSONMesssages.GameOptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class GameMenu {

    private ClientSocket cs = ClientSocket.getInstance();
    private GameOptionMessage gmo = new GameOptionMessage();
    private static final Logger log = LogManager.getLogger(GameMenu.class);

    public void display() {
        // Fetch options
        String options = getOptions();

        // Display options

        // Choose options
        //

    }

    private String getOptions() {
        cs.sendMessage(gmo.gameOptionsSend());
        JsonReader jsonReader = Json.createReader(new StringReader(cs.receiveMessage()));
        JsonObject obj = jsonReader.readObject();
        String options = "";
        if (obj.getJsonObject("available") != null) {
            JsonObject optionParent = obj.getJsonObject("available");
            int size = obj.getInt("size");
            log.info("Available poker games\n");
            for (int i = 0; i <= size; i++) {
                options += i + ". Poker game";
            }

        } else {

        }

        return options;
    }

    private void chooseOption() {

    }
}
