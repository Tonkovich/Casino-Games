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

// TODO: Implement more input checks
// TODO: Follow design practices, im crunched on time
public class GameMenu {

    private ClientSocket cs = ClientSocket.getInstance();
    private GameOptionMessage gmo = new GameOptionMessage();
    private static final Logger log = LogManager.getLogger(GameMenu.class);

    public void display() {
        // Fetch options and choose
        String options = getOptions();
        log.info(options);
    }

    private String getOptions() {
        Scanner scan = new Scanner(System.in);
        cs.sendMessage(gmo.gameOptionsSend());
        JsonReader jsonReader = Json.createReader(new StringReader(cs.receiveMessage()));
        JsonObject obj = jsonReader.readObject();
        String options = "";
        if (obj.getJsonObject("available") != null) {
            options = chooseOption(obj);
            log.info(options);
            int option = scan.nextInt();
            if (0 < option && option <= obj.getInt("size")) {
                joinGame(option);
            }

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
        for (int i = 1; i <= size; i++) {
            options += i + ". Poker game\n";
        }
        return options;
    }

    private void createGame() {
        Scanner scan = new Scanner(System.in);
        String result = scan.next();
        if (result.equalsIgnoreCase("y")) {

            System.out.println();
            System.out.print("Max Players: ");
            int maxPlayers = scan.nextInt();
            System.out.print("\nBig blind: ");
            int bigBlind = scan.nextInt();
            System.out.print("\nSmall blind: ");
            int smallBlind = scan.nextInt();

            log.info("Game being created, please wait...");
            cs.sendMessage(gmo.createGame(maxPlayers, bigBlind, smallBlind));
        } else if (result.equalsIgnoreCase("n")) {
            // TODO Maybe make something that will go back to game menu and give a choice, but for no just exit
            log.info("Client logging off..");
            System.exit(0);
        } else {
            log.info("Incorrect entry: Try again");
            createGame();
        }
    }

    private void joinGame(int option) {
        cs.sendMessage(gmo.joinGame(option));
    }
}
