package Utils;

import Models.Games.Player;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.InetAddress;
import java.util.List;

public class Database {

    private Sql2o sql2o;

    // Calling this constructor creates sql2o object
    public Database() throws DatabaseException {
        if (sql2o == null) { // Only do this once
            String jdbcURL = System.getenv("JDBC-URL");
            String jdbcUser = System.getenv("JDBC-USER");
            String jdbcPass = System.getenv("JDBC-PASS");

            if (jdbcURL == null || jdbcURL.length() == 0 || jdbcUser == null || jdbcPass == null) {
                throw new DatabaseException("Database not connecting"
                        + "\n URL: " + jdbcURL
                        + "\n User: " + jdbcUser
                        + "\n Pass: " + jdbcPass);
            }

            sql2o = new Sql2o(jdbcURL, jdbcUser, jdbcPass);
        }
    }

    public void loadPlayer(int userID, InetAddress ip) {
        String playerSQL = "SELECT user_id, wallet FROM Players p WHERE p.user_id = :userID";
        try (Connection con = sql2o.open()) {
            List<Player> player = con.createQuery(playerSQL)
                    .addParameter("userID", userID)
                    .addColumnMapping("wallet", "wallet")
                    .addColumnMapping("user_id", "userID")
                    .addColumnMapping("ip_address", ip.getHostAddress())
                    .executeAndFetch(Player.class);
        }

    }
}
