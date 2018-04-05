package Utils;

import Models.Games.Player;
import Utils.JSONMessages.LoginMessages;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.InetAddress;
import java.util.List;

public class Database {

    private Sql2o sql2o;
    private Players players = Players.getInstance();
    private LoginMessages lm = new LoginMessages();
    private static Database instance;

    public static Database getInstance() {
        return instance;
    }

    private Database() {

    }

    // Calling this constructor creates sql2o object
    public Database(String jdbcURL, String jdbcUser, String jdbcPass) throws DatabaseException {
        instance = this;
        if (sql2o == null) { // Only do this once
            if (jdbcURL == null || jdbcURL.length() == 0 || jdbcUser == null || jdbcPass == null) {
                throw new DatabaseException("Database not connecting"
                        + "\n URL: " + jdbcURL
                        + "\n User: " + jdbcUser
                        + "\n Pass: " + jdbcPass);
            }

            sql2o = new Sql2o(jdbcURL, jdbcUser, jdbcPass);
        }
    }

    // Will login player and load player from DB
    public void loginPlayer(String username, String password, InetAddress ip) {
        String playerSQL = "SELECT user_id, wallet FROM Players p WHERE p.username = :username AND p.password = :password";
        try (Connection con = sql2o.open()) {
            List<Player> player = con.createQuery(playerSQL)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addColumnMapping("username", username)
                    .addColumnMapping("wallet", "wallet")
                    .addColumnMapping("user_id", "userID")
                    .addColumnMapping("ip_address", ip.getHostAddress())
                    .executeAndFetch(Player.class);

            // Push player to Server Map and send success message
            Player newPlayer = player.get(0);
            if (newPlayer.getUserID() != 0) { // Should work to verified login worked
                players.loginPlayer(newPlayer.getUserID(), newPlayer);
                newPlayer.sendMessage(lm.successLogin());
            } else {
                // Should work since IP will still be entered but other fields should be null
                newPlayer.sendMessage(lm.incorrectLogin());
            }
        }
    }
}
