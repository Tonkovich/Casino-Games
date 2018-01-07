import GameModels.Poker;
import GameModels.Slots;

import java.util.HashMap;

public class Games {

    private static Games instance;
    public static Games getInstance(){
        if(instance == null){
            instance = new Games();
        }
        return instance;
    }

    private HashMap<Integer, Poker> pokerGames = new HashMap<>();
    private HashMap<Integer, Slots> slotGames = new HashMap<>();

    public Poker getPokerGame(int gameID) {
        return pokerGames.get(gameID);
    }

    public Slots getSlotGame(int gameID) {
        return slotGames.get(gameID);
    }

    public void createPokerGame(int initialUserID){
        /**
         * Somehow run initial code like waiting for more player, first cards, etc...
         */
        // We will use initialUserID for gameID because no one will ever have it and it's easy
        pokerGames.put(initialUserID, new Poker());
    }

    public void createSlotGame(int userID) {
        slotGames.put(userID, new Slots());
    }
}
