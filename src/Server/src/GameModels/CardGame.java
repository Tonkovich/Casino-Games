package GameModels;

import java.util.ArrayList;

public interface CardGame extends Game {

    double getPot();

    void addToPot(double amount);

    void resetPot();

    ArrayList<String> getPlayerHand(int userID);

    void setPlayerHand(ArrayList<String> playerCards, int userID);

    ArrayList<String> getHouseCards();

    void setHouseCards(ArrayList<String> houseCards);

    ArrayList<String> getHouseDeck();

    void setHouseDeck(ArrayList<String> houseDeck);
}
