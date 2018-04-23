package Parsers;

import Graphics.GameMenu;
import Graphics.UserInterface;

import javax.json.JsonObject;

public class ParseFactory {

    private static ParseFactory instance;
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
            ui.getInput(json);
        } else if (json.getJsonString("pokerMessage") != null) {
            ui.updateLog(json);
        } else if (json.getJsonString("updateGUI") != null) {
            ui.update(json);
        } else if (json.getJsonString("pokerExit") != null) {
            ui.exitGame();
        } else if (json.getJsonString("incorrectInput") != null) {
            // TODO, handled when illegal action done
        }

    }
}
