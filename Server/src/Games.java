import GameModels.*;

import java.util.HashMap;

public class Games {

    private static Games instance;
    public static Games getInstance(){
        if(instance == null){
            instance = new Games();
        }
        return instance;
    }

    private HashMap<Integer, CardGame> pokerGames = new HashMap<>();
    private HashMap<Integer, Game> slotGames = new HashMap<>();

    public CardGame getPokerGame(int gameID) {
        return pokerGames.get(gameID);
    }

    public void updatePokerGame(Poker game, int gameID) {
        pokerGames.put(gameID, game);
    }

    public Game getSlotGame(int gameID) {
        return slotGames.get(gameID);
    }

    public void updateSlotGame(Slots game, int gameID) {
        slotGames.put(gameID, game);
    }

    public void createPokerGame(int initialUserID){
        CardGame newGame = new Poker();
        /**
         * Somehow run initial code like waiting for more player, first cards, etc...
         */
        // We will use initialUserID for gameID because no one will ever have it and it's easy
        pokerGames.put(initialUserID, newGame);
    }

    public void createSlotGame(int userID) {
        Game newGame = new Slots();
        slotGames.put(userID, newGame);
    }
}
