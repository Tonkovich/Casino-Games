package GameModels;

public class Slots extends Game {
    /**
     * Will hold Hashmaps and other game data templates
     */
    private Player user;
    private int userID;

    public Player getPlayer(int userID) {
        return user;
    }

    public void addPlayer(Player newPlayer, int incomingID) {
        user = newPlayer;
        userID = incomingID;
    }
}
