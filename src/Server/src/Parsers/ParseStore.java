package Parsers;

import javax.json.JsonObject;

public class ParseStore {

    JsonObject json;
    PokerParse pp = PokerParse.getInstance();
    SlotParse sp = SlotParse.getInstance();

    private ParseStore() {
        // Do nothing
    }

    public ParseStore(JsonObject json) {
        this.json = json;
        parse();
    }

    // First line from client will send message to different parsers
    public void parse() {
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
