package Models.Games;


import Models.Parts.CardGame.Hand;

public interface CardGame extends Game {

    Hand getPlayerHand(int userID);

    void setPlayerHand(Hand playerCards, int userID);

    void deal(int userID);

    void drawNextCard();

    double getPot();

    void addToPot(double amount);

    boolean isMoveAllowed(Player player);

    void setGameReady();

    boolean isGameReady();

    void completeRound();

}
