package Parsers;

import Graphics.GameMenu;
import Models.Player;
import Utils.ClientSocket;
import Utils.JSONMesssages.HeartBeatMessage;

import javax.json.JsonObject;

public class ParseFactory {

    private static ParseFactory instance;
    private ClientSocket socket = ClientSocket.getInstance();
    private HeartBeatMessage hb = new HeartBeatMessage();
    private GameMenu gm = new GameMenu();

    private ParseFactory() {
    }

    public static ParseFactory getInstance() {
        if (instance == null) {
            instance = new ParseFactory();
        }
        return instance;
    }

    public void parse(JsonObject json) {
        if (!json.isNull("heartBeat")) {
            Player p = Player.getInstance();
            socket.sendMessage(hb.heartBeatSend(p.getUserID()));
        } else if (!json.isNull("gameOptions")) {
            gm.display(); // TODO Should be changed
        }
    }
}
