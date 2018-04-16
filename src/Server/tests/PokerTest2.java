import Models.Games.Player;
import Models.Games.Poker;
import Utils.Database.Players;
import org.junit.Test;

public class PokerTest2 {

  @Test
  public void setPlayerRolesTest() {

    Poker poker = new Poker();
    // 4 players
    Player p1 = new Player();
    Player p2 = new Player();
    Player p3 = new Player();
    Player p4 = new Player();

    poker.players.put(1, p1);
    poker.players.put(2, p2);
    poker.players.put(3, p3);
    poker.players.put(4, p4);

    poker.setPlayerRoles();

    // Check if initial roles are set properly
    for (Player player : poker.players.values()) {
      System.out.println(player.getPlayerRole());
    }

    System.out.println("/***************/");

    // Loop and set player roles many times to check if roles wrap around properly
    for (int i = 0; i < 6; i++) {
      poker.setPlayerRoles();

      System.out.println("Iteration " + i);

      for (Player player : poker.players.values()) {
        System.out.println(player.getPlayerRole());
      }
      System.out.println("/***************/");
    }
  }
}
