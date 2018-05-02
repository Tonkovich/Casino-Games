package Models.Games;

/**
 * This class will hold all data and logic pertaining to the slots game
 */
public class Slots implements MachineGame {

    private Player player;

    public void addPlayer(int userID, Player player) {
        this.player = player;
    }

    public Player getPlayer(int userID) {
        return this.player;
    }
}
