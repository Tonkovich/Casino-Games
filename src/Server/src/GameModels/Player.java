package GameModels;

import java.net.InetAddress;

public class Player {
    private double playerWallet = 0; // Should only set as zero on "new Player()"
    private InetAddress IPAddress;

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
}
