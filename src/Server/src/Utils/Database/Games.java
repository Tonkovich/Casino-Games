package Utils.Database;

import Models.Games.Poker;
import Models.Games.Slots;
import Utils.JSONMessages.GameOptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Games {

    private static final Logger log = LogManager.getLogger(Games.class);
    private GameOptionMessage gom = new GameOptionMessage();
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


    public void createPokerGame(JsonObject json) {
        Poker newGame = new Poker();

        int userID = json.getInt("userID");
        newGame.smallBlind = json.getInt("smallBlind");
        newGame.bigBlind = json.getInt("bigBlind");
        newGame.maxSetPlayer = json.getInt("maxPlayers");
        newGame.gameID = userID;
        newGame.pt.setGameID(userID);

        newGame.addPlayer(userID, playerDB.getPlayer(userID));

        // We will use initialUserID for gameID because no one will ever have it and it's easy
        pokerGames.put(userID, newGame);
        log.info("New poker game created ID:" + userID);
        //playerDB.getPlayer(userID).sendMessage(gom.startGUI(userID));
    }

    public void joinPokerGame(JsonObject json) {
        int gameID = json.getInt("gameID");
        int userID = json.getInt("userID");

        try {
            Poker game = pokerGames.get(gameID);

            // Get user from PlayerDB
            game.addPlayer(userID, playerDB.getPlayer(userID));
            log.info(playerDB.getPlayer(userID).getUsername() + " joined game ID:" + gameID);
        } catch (NullPointerException ex) {

        }
    }

    public void createSlotGame(int userID) {
        Slots newGame = new Slots();
        slotGames.put(userID, newGame);
    }

    // Returns values to be used for listing all games
    public Set<Integer> getPokerGames() {
        Set<Integer> allGames = pokerGames.keySet();
        Set<Integer> allAvailableGames = new LinkedHashSet<>();
        for (Integer i : allGames) {
            if (!pokerGames.get(i).maxPlayers) {
                allAvailableGames.add(i);
            }
        }
        return allAvailableGames;
    }

    public void closePokerGame(int gameID) {
        pokerGames.remove(gameID);
        log.info("Poker game ID:" + gameID + " closed.");
    }
}
