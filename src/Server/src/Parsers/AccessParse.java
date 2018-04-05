package Parsers;

import Utils.Database;

import javax.json.JsonObject;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AccessParse {
    private static AccessParse instance;
    private static Database db;

    private AccessParse() {
    }

    public static AccessParse getInstance() {
        if (instance == null) {
            instance = new AccessParse();
            db = Database.getInstance();
        }
        return instance;
    }

    public void parse(JsonObject obj) {
        String username = obj.getString("username");
        String password = obj.getString("password");
        try {
            InetAddress ip = InetAddress.getByName(obj.getString("IPAddress"));
            db.loginPlayer(username, password, ip);
        } catch (UnknownHostException ex) {
            // TODO: Stop parse
        }
    }
}
