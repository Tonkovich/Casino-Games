package Parsers;

import Utils.Database;

import javax.json.JsonObject;

public class AccessParse {
    private static AccessParse instance;
    private static Database db = Database.getInstance();


    private AccessParse() {
    }

    public static AccessParse getInstance() {
        if (instance == null) {
            instance = new AccessParse();
        }
        return instance;
    }

    public void parse(JsonObject obj, String ip) {
        String username = obj.getString("username");
        String password = obj.getString("password");
        db.loginPlayer(username, password, ip);
    }
}
