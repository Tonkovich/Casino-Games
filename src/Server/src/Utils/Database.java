package Utils;

import Models.Games.Player;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Database {

    private Sql2o sql2o;

    public List<Player> loadPlayer(int userID) {
        String playerSQL = "SELECT user_id, wallet FROM Players p WHERE p.user_id = :userID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(playerSQL)
                    .addParameter("userID", userID)
                    .addColumnMapping("wallet", "wallet")
                    .addColumnMapping("user_id", "userID")
                    .executeAndFetch(Player.class);
        }
    }
}
