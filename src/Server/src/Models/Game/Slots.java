package Models.Game;

import java.util.Timer;

/**
 * This class will hold all data and logic pertaining to the slots game
 */
public class Slots implements MachineGame {

    private Player player;
    private Timer timer;

    public void addPlayer(int userID, Player player) {
        this.player = player;
    }

    public Player getPlayer(int userID) {
        return this.player;
    }
}
