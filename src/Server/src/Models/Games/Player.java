package Models.Games;

import Utils.Packet.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;

public class Player {
    private ServerSocket socket = ServerSocket.getInstance();
    private double wallet = 0; // Should only set as zero on "new Player()"
    private String ip;
    private int port;
    private int heartBeatPort;
    private int userID;
    private String username;
    private String playerRole;
    private boolean ready = false;

    public double getPlayerWallet() {
        return wallet;
    }

    public void setPlayerWallet(double amount) {
        wallet = amount;
    }

    public String getIPAddress() {
        return ip;
    }

    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public void setIPAddress(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setHeartBeatPort(int heartBeatPort) {
        this.heartBeatPort = heartBeatPort;
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
            socket.sendMessage(InetAddress.getByName(getIPAddress()), port, json);
        } catch (IOException e) {
            System.out.println("Player sendMessage Failed");
        }
    }

    public void sendHeartBeat(String json) {
        try {
            socket.sendMessage(InetAddress.getByName(getIPAddress()), heartBeatPort, json);
        } catch (IOException e) {
            System.out.println("Player sendHeartBeat Failed");
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }


//    public int compareTo(Player anotherPlayer) {
//        return userID - anotherPlayer.getUserID();
//    }
}