import Models.Games.Player;
import Models.Games.Poker;
import Utils.Players;
import org.junit.Test;

public class PokerTest {

    @Test
    public void cardRankTest() {
        Players playersDB = Players.getInstance(); // Simulate player login

        for (int i = 0; i < 20; i++) {
            Player user1 = new Player();
            user1.setUserID(1);
            user1.setUsername("Player 1");
            Player user2 = new Player();
            user2.setUserID(2);
            user2.setUsername("Player 2");
            Player user3 = new Player();
            user3.setUserID(3);
            user3.setUsername("Player 3");
            playersDB.loginPlayer(1, user1);
            playersDB.loginPlayer(2, user2);
            playersDB.loginPlayer(3, user3); // All players logged in

            Poker poker = new Poker(); // Game instance started

            poker.addPlayer(1, user1);
            poker.addPlayer(2, user2);
            poker.addPlayer(3, user3); // All players have joined

            poker.deal(); // Start game

            poker.drawNextCard();
            poker.drawNextCard(); // River done
            poker.getWinner();
            System.out.print("\n\n"); // Space out each test
        }
    }
}
