import Models.Games.Player;
import org.junit.Test;

public class PokerTest {

    @Test
    public void cardRankTest() {
        //Players playersDB = Players.getInstance(); // Simulate player login

        for (int i = 0; i < 5; i++) {
            Player user1 = new Player();
            user1.setUserID(1);
            user1.setUsername("Player 1");
            Player user2 = new Player();
            user2.setUserID(2);
            user2.setUsername("Player 2");
            Player user3 = new Player();
            user3.setUserID(3);
            user3.setUsername("Player 3");
            //playersDB.loginPlayer(1, user1);
            //playersDB.loginPlayer(2, user2);
            //playersDB.loginPlayer(3, user3); // All players logged in

//            Poker poker = new Poker(); // Game instance started
//
//            try {
//                poker.addPlayer(1, user1);
//                poker.addPlayer(2, user2);
//                poker.addPlayer(3, user3); // All players have joined
//            } catch (NullPointerException ex) {
//                System.out.println("Comment out message sender to test.");
//            }
//
//            //poker.deal(); // Start game
//
//            poker.drawNextCard();
//            poker.drawNextCard(); // River done
//            poker.getWinner();
            System.out.print("\n\n"); // Space out each test
        }
    }
}
