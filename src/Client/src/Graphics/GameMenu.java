package Graphics;

import Utils.ClientSocket;
import Utils.JSONMesssages.GameOptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Scanner;

public class GameMenu {

    private ClientSocket cs = ClientSocket.getInstance();
    private GameOptionMessage gmo = new GameOptionMessage();
    private static final Logger log = LogManager.getLogger(GameMenu.class);

    public void display() {
        // Fetch options
        String options = getOptions();
        log.info(options);

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
            options = chooseOption(obj);
            log.info(options);

        } else if (obj.getString("createGame") != null) {
            log.info("No games available..");
            log.info("Would you like to create one? (y/n)");
            createGame();
        }

        return options;
    }

    private String chooseOption(JsonObject obj) {
        String options = "";
        int size = obj.getInt("size");
        log.info("Available poker games\n");
        for (int i = 0; i <= size; i++) {
            options += i + ". Poker game\n";
        }
        return options;
    }

    private void createGame() {
        Scanner scan = new Scanner(System.in);
        String result = scan.next();
        if (result.equalsIgnoreCase("y")) {
            // TODO Create game
            /**
             *
             *
             *
             * Create Game logic here
             * Max players, bigBlind, small blind, etc..
             *
             *
             *
             *
             */
        } else if (result.equalsIgnoreCase("n")) {
            // TODO Maybe make something that will go back to game menu and give a choice, but for no just exit
            log.info("Client logging off..");
            System.exit(0);
        } else {
            log.info("Incorrect entry: Try again");
            createGame();
        }
    }
}
