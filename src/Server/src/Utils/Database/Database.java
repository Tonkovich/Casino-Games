package Utils.Database;

import Models.Games.Player;
import Utils.JSONMessages.LoginMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Database {

    private Sql2o sql2o;
    private Players players = Players.getInstance();
    private LoginMessages lm = new LoginMessages();
    private static Database instance;
    private static final Logger log = LogManager.getLogger(Database.class);

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {

    }

    // Calling this constructor creates sql2o object
    public void startConnection(String jdbcURL, String jdbcUser, String jdbcPass) throws DatabaseException {
        if (sql2o == null) { // Only do this once
            if (jdbcURL == null || jdbcURL.length() == 0 || jdbcUser == null || jdbcPass == null) {
                throw new DatabaseException("Database not connecting, check config.json"
                        + "\n URL: " + jdbcURL
                        + "\n User: " + jdbcUser
                        + "\n Pass: " + jdbcPass);
            }
            sql2o = new Sql2o(jdbcURL, jdbcUser, jdbcPass);
            log.info("Database connected");
        }
    }

    // Will login player and load player from DB
    public void loginPlayer(String username, String password, String ip, int port) {
        updateIP(ip, username);
        String playerSQL = "SELECT user_id, ip, username, wallet FROM Players p WHERE p.username = :username AND p.password = :password";
        try (Connection con = sql2o.open()) {
            List<Player> player = con.createQuery(playerSQL)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addColumnMapping("username", "username")
                    .addColumnMapping("wallet", "wallet")
                    .addColumnMapping("user_id", "userID")
                    .addColumnMapping("ip", "ip")
                    .executeAndFetch(Player.class);

            // Push player to Server Map and send success message
            Player newPlayer = new Player();
            if (player.size() == 1) {
                newPlayer = player.get(0);
                newPlayer.setPort(port);
                newPlayer.sendMessage(lm.successLogin(newPlayer.getUserID(), newPlayer.getPlayerWallet())); // First send success
                players.loginPlayer(newPlayer.getUserID(), newPlayer); // Then heart beat, order must be kept
                log.info(newPlayer.getUsername() + " has logged in.");
            } else {
                newPlayer.setIPAddress(ip);
                newPlayer.sendMessage(lm.incorrectLogin());
            }
        }
    }

    private void updateIP(String ip, String username) {
        String sql = "UPDATE Players p SET p.ip = :ip WHERE p.username = :username";
        Connection con = sql2o.open();
        con.createQuery(sql)
                .addParameter("username", username)
                .addParameter("ip", ip)
                .executeUpdate();
    }
}
