package Parsers;

import Utils.Database.Games;
import Utils.Packet.HeartBeat;

import javax.json.JsonObject;

public class ParseStore {

    private static ParseStore instance;
    private Games gameDB = Games.getInstance();
    private SlotParse sp = SlotParse.getInstance();
    private HeartBeat hb = HeartBeat.getInstance();
    private PokerParse pp = PokerParse.getInstance();
    private AccessParse ap = AccessParse.getInstance();
    private GameOptionsParse gop = GameOptionsParse.getInstance();

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
            if (json.getJsonString("gameAction") != null) {
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
            } else if (json.getJsonString("createGame") != null) {
                gameDB.createPokerGame(json);
            } else if (json.getJsonString("joinGame") != null) {
                gameDB.joinPokerGame(json);
            } else if (json.getJsonString("heartBeat") != null) {
                hb.receive(json);
            } else if (json.getJsonString("gameOptions") != null) {
                // Client requesting all available game options
                gop.parse(json);
            }
        }
    }


}
