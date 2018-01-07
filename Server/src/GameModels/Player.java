package GameModels;

public class Player {
    private double playerWallet = 0; // Should only set as zero on "new Player()"

    public double getPlayerWallet() {
        /**
         * Waiting until database is setup to figure out our process
         */
        return playerWallet;
    }

    public void addToPlayerWallet(double amount) {
        playerWallet = playerWallet + amount;
    }
}
