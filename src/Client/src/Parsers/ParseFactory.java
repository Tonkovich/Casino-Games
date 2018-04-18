package Parsers;

import Graphics.GameMenu;
import Graphics.UserInterface;
import Utils.ClientSocket;
import Utils.JSONMesssages.HeartBeatMessage;

import javax.json.JsonObject;

public class ParseFactory {

    private static ParseFactory instance;
    private ClientSocket socket = ClientSocket.getInstance();
    private HeartBeatMessage hb = new HeartBeatMessage();
    private GameMenu gm = new GameMenu();
    private UserInterface ui = UserInterface.getInstance();

    private ParseFactory() {
    }

    public static ParseFactory getInstance() {
        if (instance == null) {
            instance = new ParseFactory();
        }
        return instance;
    }

    public void parse(JsonObject json) {
        if (json.getJsonString("gameOptions") != null) {
            gm.display(); // TODO Should be changed
        } else if (json.getJsonString("pokerAction") != null) {
            // TODO: Trigger user input, send to server, retrieve update
        } else if (json.getJsonString("pokerMessage") != null) {
            // TODO: Send to gameLog
        } else if (json.getJsonString("startGUI") != null) {
            // Called once the server has confirmed the client is in the game
            //ui.start(json);

        } else if (json.getJsonString("updateGUI") != null) {
            System.out.println("Test");
            ui.update(json);
        }

    }
}
