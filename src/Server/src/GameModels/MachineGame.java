package GameModels;

public class MachineGame extends Game {
    private Player user;

    public Player getPlayer() {
        return user;
    }

    public void addPlayer(Player newPlayer) {
        user = newPlayer;
    }
}
