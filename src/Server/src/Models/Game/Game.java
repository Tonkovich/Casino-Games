package Models.Game;

public interface Game {
    void addPlayer(int userID, Player player);

    Player getPlayer(int userID);
}
