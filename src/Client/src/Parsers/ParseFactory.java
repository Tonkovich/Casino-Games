package Parsers;

import Models.Player;
import Utils.ClientSocket;
import Utils.JSONMesssages.HeartBeatMessage;

import javax.json.JsonObject;
import java.io.IOException;

public class ParseFactory {

    private static ParseFactory instance;
    private ClientSocket socket = ClientSocket.getInstance();
    private HeartBeatMessage hb = new HeartBeatMessage();

    private ParseFactory() {
    }

    public static ParseFactory getInstance() {
        if (instance == null) {
            instance = new ParseFactory();
        }
        return instance;
    }

    public void parse(JsonObject json) {
        try {
            if (!json.isNull("heartBeat")) {
                Player p = Player.getInstance();
                socket.sendMessage(hb.heartBeatSend(p.getUserID()));
            }
        } catch (IOException ex) {
        }


    }
}
