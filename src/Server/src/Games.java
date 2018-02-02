import Models.Games.Poker;
import Models.Games.Slots;

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

    public void updatePokerGame(Poker game, int gameID) {
        pokerGames.put(gameID, game);
    }

    public Slots getSlotGame(int gameID) {
        return slotGames.get(gameID);
    }

    public void updateSlotGame(Slots game, int gameID) {
        slotGames.put(gameID, game);
    }

    public void createPokerGame(int initialUserID){
        Poker newGame = new Poker();

        // We will use initialUserID for gameID because no one will ever have it and it's easy
        pokerGames.put(initialUserID, newGame);
    }

    public void joinPokerGame(int userID, int gameID) {
        Poker game = pokerGames.get(gameID);
        //Player player = Database.getPlayer() wait until method is created
        //game.addPlayer(userID, Player);
        /**
         * initial code like get cards...etc...
         */
        updatePokerGame(game, gameID);
    }

    public void createSlotGame(int userID) {
        Slots newGame = new Slots();
        slotGames.put(userID, newGame);
    }
}
