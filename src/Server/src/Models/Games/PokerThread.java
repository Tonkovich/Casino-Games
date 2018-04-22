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


    public PokerThread(int gameID) {
        this.gameID = gameID;
    }


    @Override
    public void run() {
        pk = gameDB.getPokerGame(gameID);
        //while(true) {
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
        completeGame();
        //if(newGame()){
        //   break;
        //}
        // Send UI update to players to go back to game menu
        //}
    }

    private void applyBlinds() {
        pk.setPlayerRoles();
        // Blinds posted after initial assigning, modify maxBet
        for (Player p : pk.players.values()) {
            if (p.getPlayerRole().equals("Big Blind")) {
                pk.addToPot(pk.bigBlind, p.getUserID());
            } else if (p.getPlayerRole().equals("Small Blind")) {
                pk.addToPot(pk.smallBlind, p.getUserID());
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
            while (responded == false) {
                // loop used to wait for response
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {

                }
                // TODO: Have timer limit, if reached kick player pk.removePlayer() or fold automatically
            }
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

    private void completeGame() {
        pk.completeGame();
    }

    private boolean newGame() {
        return true;
    }


}
