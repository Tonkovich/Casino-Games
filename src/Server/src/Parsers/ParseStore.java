package Parsers;

import javax.json.JsonObject;

public class ParseStore {

    private static ParseStore instance;
    private PokerParse pp = PokerParse.getInstance();
    private SlotParse sp = SlotParse.getInstance();

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
            switch (json.getString("gameType")) {
                case "Poker":
                    pp.parse(json);
                case "Slots":
                    sp.parse(json);
            }
        }
    }


}
