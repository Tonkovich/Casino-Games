package Models;

public class Player {

    private static Player instance;

    public static Player getInstance() {
        if (instance == null) {
            return instance = new Player();
        }
        return instance;
    }

    private Player() {

    }

    private double playerWallet = 0; // Should only set as zero on "new Models.Player()"
    private int userID;
    private String username;

    public double getPlayerWallet() {
        return playerWallet;
    }

    public void setPlayerWallet(double amount) {
        playerWallet = amount;
    }

    public void setUserID(int incoming) {
        userID = incoming;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
