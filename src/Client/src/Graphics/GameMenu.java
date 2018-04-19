package Graphics;

import Utils.ClientSocket;
import Utils.JSONMesssages.GameOptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

// TODO: Implement more input checks
// TODO: Follow design practices, im crunched on time
public class GameMenu {

    private ClientSocket cs = ClientSocket.getInstance();
    private UserInterface ui = UserInterface.getInstance();
    private GameOptionMessage gmo = new GameOptionMessage();
    private static final Logger log = LogManager.getLogger(GameMenu.class);
    private ArrayList<Integer> allGameIDs = new ArrayList<>();

    public void display() {
        // Fetch options and choose
        getOptions();
    }

    private void getOptions() {
        Scanner scan = new Scanner(System.in);
        cs.sendMessage(gmo.gameOptionsSend());
        JsonReader jsonReader = Json.createReader(new StringReader(cs.receiveMessage()));
        JsonObject obj = jsonReader.readObject();
        if (obj.getJsonObject("available") != null) {
            String options = chooseOption(obj);
            log.info(options);
            log.info("Please enter the lobby's number to join: ");
            int option = scan.nextInt();
            if (allGameIDs.contains(option)) {
                joinGame(option);
                ui.gameID = option;
            } else {
                // Incorrect choice
                incorrectOption(obj);
            }

        } else if (obj.getString("createGame") != null) {
            log.info("No games available..");
            log.info("Would you like to create one? (y/n)");
            createGame();
        }
        scan.close();
    }

    private String chooseOption(JsonObject obj) {
        String options = "";
        int size = obj.getInt("size");
        JsonObject available = obj.getJsonObject("available");

        log.info("Available poker games\n");
        for (int i = 0; i < size; i++) {
            options += available.getInt("" + i) + ". Poker game\n";
            allGameIDs.add(available.getInt("" + i));
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
            if (maxPlayers > 4) {
                System.out.println("Server only allows 4 max..setting to 4");
                maxPlayers = 4;
            }
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
        scan.close();
    }

    private void joinGame(int option) {
        cs.sendMessage(gmo.joinGame(option));
    }

    private void incorrectOption(JsonObject obj) {
        Scanner scan = new Scanner(System.in);
        log.info("Wrong selection try again: ");
        String options = chooseOption(obj);
        log.info(options);
        int option = scan.nextInt();
        if (allGameIDs.contains(option)) {
            scan.close();
            joinGame(option);
        } else {
            // Incorrect choice
            incorrectOption(obj);
        }
    }
}
