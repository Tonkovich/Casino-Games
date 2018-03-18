package Models.Games;

import Utils.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Player implements Comparable<Player> {
    private double playerWallet = 0; // Should only set as zero on "new Player()"
    private String IPAddress;
    private int userID;
    private String username;

    public double getPlayerWallet() {
        /**
         * Waiting until database is setup to figure out our process
         */
        return playerWallet;
    }

    public void setPlayerWallet(double amount) {
        playerWallet = amount;
    }

    public InetAddress getIPAddress() {
        try {
            return InetAddress.getByName(IPAddress);
        } catch (UnknownHostException ex) {
            return null; // is this okay?
        }
    }

    public void setIPAddress(InetAddress IPAddress) {
        this.IPAddress = IPAddress.getHostAddress();
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
            client.sendMessage(InetAddress.getByName(IPAddress), 1337, json);
        } catch (IOException e) {
            // Do nothing
        }
    }

    public int compareTo(Player anotherPlayer) {
        return userID - anotherPlayer.getUserID();
    }
}
