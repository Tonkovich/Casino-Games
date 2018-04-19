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
            gm.display();
        } else if (json.getJsonString("pokerAction") != null) {
            ui.getInput(json.getBoolean("otherUserBet"), json);
        } else if (json.getJsonString("pokerMessage") != null) {
            ui.updateLog(json);
        } else if (json.getJsonString("updateGUI") != null) {
            ui.update(json);
        }

    }
}
