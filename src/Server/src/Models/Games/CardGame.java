package Models.Games;

import Models.Parts.CardGame.Deck;
import Models.Parts.CardGame.Hand;

public interface CardGame extends Game {

    double getPot();

    void addToPot(double amount);

    void resetPot();

    Hand getPlayerHand(int userID);

    void setPlayerHand(Hand playerCards, int userID);

    Deck getDeck();

    void setDeck(Deck houseDeck);
}
