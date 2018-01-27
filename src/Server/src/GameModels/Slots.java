package GameModels;

public class Slots implements MachineGame {

    private Player player;

    public void addPlayer(int userID, Player player) {
        this.player = player;
    }

    public Player getPlayer(int userID) {
        return this.player;
    }
}
