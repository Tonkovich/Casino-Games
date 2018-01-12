package GameModels;

import java.util.ArrayList;

public abstract class CardGame extends Game {

    public abstract Player getPlayer(int userID);

    public abstract void addPlayer(Player newPlayer, int userID);

    public abstract void setPlayerHand(ArrayList<String> playerCards, int userID);

    public abstract ArrayList<String> getPlayerHand(int userID);

    public abstract double getPot();

    public abstract void addToPot(double amount);

    public abstract void resetPot();

    public abstract ArrayList<String> getHouseCards();

    public abstract void setHouseCards(ArrayList<String> houseCards);

    public abstract ArrayList<String> getHouseDeck();

    public abstract void setHouseDeck(ArrayList<String> houseDeck);
}
