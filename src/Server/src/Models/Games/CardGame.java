package Models.Games;


import Models.Parts.CardGame.Hand;

public interface CardGame extends Game {

    Hand getPlayerHand(int userID);

    void setPlayerHand(Hand playerCards, int userID);

    void deal(int userID);

    void drawNextCard();

    double getPot();

    void addToPotCall(int userID);

    void addToPotRaise(double amount, int userID);

    void addToPotBet(double amount, int userID);

    void addToPotBlinds(double amount, int userID);

    void check(int userID);

    void completeGame();

}
