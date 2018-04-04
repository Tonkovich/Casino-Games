package Parsers;

import javax.json.JsonObject;

public class ParseFactory {

    private static ParseFactory instance;

    private ParseFactory() {
    }

    public static ParseFactory getInstance() {
        if (instance == null) {
            instance = new ParseFactory();
        }
        return instance;
    }

    public void parse(JsonObject json) {

    }
}
