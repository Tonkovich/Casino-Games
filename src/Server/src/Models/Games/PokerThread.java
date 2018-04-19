package Models.Games;

import Utils.Database.Games;

public class PokerThread implements Runnable {

    private Games gameDB = Games.getInstance();
    private int gameID;
    private Poker pk;

    public PokerThread() {
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public void run() {
        pk = gameDB.getPokerGame(gameID);
        while (true) {
            /**
             *
             * Thread code here
             *
             *
             */
        }
    }
}
