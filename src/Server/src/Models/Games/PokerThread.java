package Models.Games;

import Utils.Database.Games;
import Utils.JSONMessages.PokerMessages;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class PokerThread implements Runnable {

    private static Games gameDB = Games.getInstance();
    private PokerMessages pm = new PokerMessages();
    private Poker pk;
    private int gameID;
    public boolean responded = false;
    private double maxBet = 0;
    private boolean allReady;


    public PokerThread(int gameID) {
        this.gameID = gameID;
    }


    @Override
    public void run() {
        pk = gameDB.getPokerGame(gameID);
        boolean allButOneFolded = false;
        //checkIfAllReady(); // This will pause game until all players ready
        applyBlinds();
        for (int i = 0; i < 3; i++) {
            if (!allButOneFolded()) { // Initial stop
                betting();
                if (i != 0) {
                    nextRound();
                } else {
                    pk.initHouseCard();
                }
            } else {
                allButOneFolded = true;
            }
        }
        if (!allButOneFolded) {
            betting(); // All five cards down, final betting
            showOtherUsersCards();
            pk.completeGame();
        } else {
            int userID = 0;
            for (Player p : pk.players.values()) {
                if (!p.isFolded()) {
                    userID = p.getUserID();
                }
            }
            showOtherUsersCards();
            pk.winByAllFolded(userID);
        }

        // Ask players if the wanna play again
        if (checkIfAllReady()) {
            pk.startNewGame();
        } else {
            pk.exitGame();
        }
    }

    private void applyBlinds() {
        pk.setPlayerRoles();
        // Blinds posted after initial assigning, modify maxBet
        for (Player p : pk.players.values()) {
            if (p.getPlayerRole().equals("Big Blind")) {
                pk.addToPotBlinds(pk.bigBlind, p.getUserID());
            } else if (p.getPlayerRole().equals("Small Blind")) {
                pk.addToPotBlinds(pk.smallBlind, p.getUserID());
            }
        }
    }

    private void betting() {
        Player p;
        for (Integer i : pk.players.keySet()) {
            //ask for input
            p = pk.players.get(i);
            if (!p.isFolded() && !p.isAllIn() && !allButOneFolded()) { // Skip players that have folded or is all in
                if (pk.playerBets.get(i) < maxBet) {
                    pk.askPlayerForInput(pm.betWithCall(), i);
                    responded = false;
                } else if (pk.playerBets.get(i) == maxBet) {
                    pk.askPlayerForInput(pm.betWithCheck(), i);
                    responded = false;
                } else {
                    pk.askPlayerForInput(pm.betWithCheck(), i);
                    responded = false;
                }
                while (!responded) {
                    // loop used to wait for response
                    try {
                        sleep(1); // Keeps this loop alive, no idea why
                    } catch (InterruptedException ex) {

                    }
                }
            }
            // Find max bet
            for (Double j : pk.playerBets.values()) {
                maxBet = j > maxBet ? j : maxBet;
            }
        }

        // Checks to see if all users bets are the same, ignores all ins and folds
        boolean counter = false;
        int currentID = 0;
        List<Integer> ids = new ArrayList<>(pk.playerBets.keySet());
        for (Double d : pk.playerBets.values()) {
            boolean isNotFolded = !pk.players.get(ids.get(currentID)).isFolded();
            boolean isNotAllIn = !pk.players.get(ids.get(currentID)).isAllIn();
            if (d != maxBet && isNotFolded && isNotAllIn) {
                counter = true;
            }
            currentID++;
        }

        // Not all equal, bet again
        if (counter && !allButOneFolded()) {
            betting();
        }
    }

    private void nextRound() {
        pk.drawNextCard();
    }

    private boolean checkIfAllReady() {
        while (!allReady) {
            if (!(pk.players.size() > 1)) {
                return false; // Not enough players exit
            }
            int counter = 0;
            for (Player p : pk.players.values()) {
                if (p.isReady()) {
                    counter++;
                }
            }
            if (counter == pk.players.size()) {
                allReady = true;
            }
        }
        return true;
    }

    private boolean allButOneFolded() {
        int counter = 0;
        for (Player p : pk.players.values()) {
            if (p.isFolded()) {
                counter++;
            }
        }
        // All but one folded, should end game
        return counter == (pk.players.size() - 1);
    }

    private void showOtherUsersCards() {
        pk.gameDone = true;
        pk.updateClients(); // Show all hands while waiting
    }
}
