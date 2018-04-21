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
        initialBetting();
        for (int i = 0; i < 3; i++) {
            if (i != 0) {
                betting();
            }
            nextRound();
        }
        betting(); // All five cards down, final betting
        completeGame();
    }

    public void initialBetting() {

    }

    public void betting() {

    }

    public void nextRound() {

    }

    public void completeGame() {

    }

}
