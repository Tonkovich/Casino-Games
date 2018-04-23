package Models;

import Graphics.Parts.Hand;

public class OtherPlayer {
    private double playerWallet = 0; // Should only set as zero on "new Models.Player()"
    private int userID;
    private double betAmount;
    private String username;
    public Hand hand;
    private double currentBet;

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

    public void setBetAmount(double amount) {
        betAmount = amount;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(double amount) {
        currentBet = amount;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
