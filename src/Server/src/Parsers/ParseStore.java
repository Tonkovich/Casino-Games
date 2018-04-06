package Parsers;

import Utils.HeartBeat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;

public class ParseStore {

    private static ParseStore instance;
    private PokerParse pp = PokerParse.getInstance();
    private SlotParse sp = SlotParse.getInstance();
    private AccessParse ap = AccessParse.getInstance();
    private HeartBeat hb = HeartBeat.getInstance();
    private static final Logger log = LogManager.getLogger(ParseStore.class);

    private ParseStore() {
        // Do nothing
    }

    public static ParseStore getInstance() {
        if (instance == null) {
            instance = new ParseStore();
        }
        return instance;
    }

    // First line from client will send message to different parsers
    public void parse(JsonObject json, String ip, int port) {
        if (json != null) {
            // Game actions input here
            if (json.getJsonObject("gameAction") != null) {
                switch (json.getString("gameType")) {
                    case "Poker":
                        pp.parse(json);
                        break;
                    case "Slots":
                        sp.parse(json);
                        break;
                    default:
                        break;
                }
            }
            // Login player
            else if (json.getJsonObject("login") != null) {
                ap.parse(json.getJsonObject("login"), ip, port);
                //log.info("Login attempt by: " + ip);
            } else if (json.getString("heartBeat") != null) {
                hb.receive(json);
            }
        }
    }


}
