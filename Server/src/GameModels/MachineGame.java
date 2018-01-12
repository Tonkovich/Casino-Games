package GameModels;

public abstract class MachineGame extends Game {

    public abstract Player getPlayer(int userID);

    public abstract void addPlayer(Player newPlayer, int userID);

}
