package Utils.Database;

import Models.Games.Poker;
import Models.Games.Slots;

import java.util.HashMap;
import java.util.Set;

public class Games {

    private Players playerDB = Players.getInstance();

    private static Games instance;

    public static Games getInstance(){
        if(instance == null){
            instance = new Games();
        }
        return instance;
    }

    private Games() {
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
        Poker newGame = new Poker();

        // We will use initialUserID for gameID because no one will ever have it and it's easy
        pokerGames.put(initialUserID, newGame);
    }

    public void joinPokerGame(int userID, int gameID) {
        Poker game = pokerGames.get(gameID);

        // Get user from PlayerDB
        pokerGames.get(gameID).addPlayer(userID, playerDB.getPlayer(userID));
    }

    public void createSlotGame(int userID) {
        Slots newGame = new Slots();
        slotGames.put(userID, newGame);
    }

    // Returns values to be used for listing all games
    public Set<Integer> getPokerGames() {
        return pokerGames.keySet();
    }
}
