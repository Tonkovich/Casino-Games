package Models.Games;

import Utils.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;

public class Player {
    private double playerWallet = 0; // Should only set as zero on "new Player()"
    private InetAddress IPAddress;
    private int userID;
    private String username;

    public double getPlayerWallet() {
        /**
         * Waiting until database is setup to figure out our process
         */
        return playerWallet;
    }

    public void addToPlayerWallet(double amount) {
        playerWallet += amount;
    }

    public void subtractFromPlayerWallet(double amount) {
        playerWallet -= amount;
    }

    public InetAddress getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(InetAddress IPAddress) {
        this.IPAddress = IPAddress;
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

    public void sendMessage(String json) {
        try {
            ServerSocket client = new ServerSocket(12000);
            client.sendMessage(IPAddress, 1337, json);
        } catch (IOException e) {
            // Do nothing
        }
    }
}
