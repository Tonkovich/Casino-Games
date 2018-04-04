package Parsers;

import Utils.HeartBeat;

import javax.json.JsonObject;

public class ParseStore {

    private static ParseStore instance;
    private PokerParse pp = PokerParse.getInstance();
    private SlotParse sp = SlotParse.getInstance();
    private AccessParse ap = AccessParse.getInstance();
    private HeartBeat hb = HeartBeat.getInstance();

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
    public void parse(JsonObject json) {
        if (json != null) {
            // Game actions input here
            switch (json.getString("gameType")) {
                case "Poker":
                    pp.parse(json);
                case "Slots":
                    sp.parse(json);
            }
            // Login player
            if (json.getJsonObject("login") != null) {
                ap.parse(json.getJsonObject("login"));
            } else if (json.getJsonObject("heartbeat") != null) {
                hb.receive(json.getJsonObject("heartbeat"));
            }
        }
    }


}
