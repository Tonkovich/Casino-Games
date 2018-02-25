package Parsers;

import javax.json.JsonObject;

public class SlotParse {

    private static SlotParse instance;

    private SlotParse() {
    }

    public static SlotParse getInstance() {
        if (instance == null) {
            instance = new SlotParse();
        }
        return instance;
    }

    /**
     * Will parse client input
     *
     * @param json incoming user message
     */
    public void parse(JsonObject json) {
        int gameID = json.getInt("gameID");

    }
}
