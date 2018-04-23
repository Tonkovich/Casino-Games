package Models.Games;

import Utils.Database.Games;
import Utils.JSONMessages.PokerMessages;

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
        while (true) {
            //checkIfAllReady(); // This will pause game until all players ready
            applyBlinds();
            for (int i = 0; i < 3; i++) {
                betting();
                if (i != 0) {
                    nextRound();
                } else {
                    pk.initHouseCard();
                }
            }
            betting(); // All five cards down, final betting
            pk.completeGame();
            if (checkIfAllReady()) {
                pk.startNewGame();
            } else {
                pk.exitGame();
                break;
            }
        }
    }

    private void applyBlinds() {
        pk.setPlayerRoles();
        // Blinds posted after initial assigning, modify maxBet
        for (Player p : pk.players.values()) {
            if (p.getPlayerRole().equals("Big Blind")) {
                pk.addToPotBet(pk.bigBlind, p.getUserID());
            } else if (p.getPlayerRole().equals("Small Blind")) {
                pk.addToPotBet(pk.smallBlind, p.getUserID());
            }
        }
    }

    private void betting() {
        for (Integer i : pk.players.keySet()) {
            //ask for input
            if (pk.playerBets.get(i) < maxBet) {
                pk.askPlayerForInput(pm.betWithCall(), i);
                responded = false;
                // TODO: start time
            } else {
                pk.askPlayerForInput(pm.betWithCheck(), i);
                responded = false;
                // TODO: start time
            }
            while (!responded) {
                // loop used to wait for response
                try {
                    Thread.sleep(1); // Keeps this loop alive, no idea why
                } catch (InterruptedException ex) {

                }
                // TODO: Have timer limit, if reached kick player pk.removePlayer() or fold automatically
            }
            // Find max bet
            for (Double j : pk.playerBets.values()) {
                maxBet = j > maxBet ? j : maxBet;
            }
        }
        boolean counter = false;
        for (Double d : pk.playerBets.values()) {
            if (d != maxBet)
                counter = true;
        }
        if (counter) {
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
}
