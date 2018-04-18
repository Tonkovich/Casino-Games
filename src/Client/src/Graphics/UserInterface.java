package Graphics;

import Graphics.Parts.*;
import Models.OtherPlayer;
import Models.Player;

import javax.json.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UserInterface {

    private static UserInterface instance;
    private ConsoleHelper console;
    private GameLog log = new GameLog(5);
    private Player p = Player.getInstance();
    private GameBoard gb = new GameBoard(Player.getInstance());

    private UserInterface() {
    }

    public static UserInterface getInstance() {
        if (instance == null) {
            instance = new UserInterface();
        }
        return instance;
    }

    /*public void start(JsonObject json) {
        startConsole();
        gb.bigBlind = json.getInt("bigBlind");
        gb.smallBlind = json.getInt("smallBlind");

        // Welcome message
        log.add("Welcome to Texas Hold'em heads-up tournament style! We'll be");
        log.add("playing by standard rules. ");

        Hand blankHand = new Hand();
        blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
        blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
        p.hand = blankHand;

        draw(gb, log);
    }*/

    /**
     * Updates the entire GUI for any possible changes
     *
     * @param json GUI data
     */
    public void update(JsonObject json) {
        // If update is called and client i
        if (console == null) {
            startConsole(); // If client is just joining, start first, then update
            log.add("Welcome to Texas Hold'em heads-up tournament style! We'll be");
            log.add("playing by standard rules. ");
        }
        console.clear();
        gb.pot = json.getJsonNumber("pot").doubleValue();
        gb.smallBlind = json.getInt("smallBlind");
        gb.bigBlind = json.getInt("bigBlind");

        // Assemble players cards
        JsonObject playerHand = json.getJsonObject("playerHand");

        JsonObject playerCard1 = playerHand.getJsonObject("card1");
        JsonObject playerCard2 = playerHand.getJsonObject("card2");

        Suit card1Suit = Suit.getByName(playerCard1.getJsonString("suit").getString());
        Rank card1Rank = Rank.getByVal(playerCard1.getInt("value"));
        Card card1 = new Card(card1Suit, card1Rank, false);

        Suit card2Suit = Suit.getByName(playerCard2.getJsonString("suit").getString());
        Rank card2Rank = Rank.getByVal(playerCard2.getInt("value"));
        Card card2 = new Card(card2Suit, card2Rank, false);

        // Create player hand
        Player player = Player.getInstance();
        player.hand = new Hand(card1, card2);

        // Assemble other players
        int numOfPlayers = json.getInt("numberOfPlayers");
        List<OtherPlayer> otherPlayers = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            otherPlayers.add(new OtherPlayer());
            Hand blankHand = new Hand();
            blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
            blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
            otherPlayers.get(i).hand = blankHand;
        }

        gb.otherPlayers = otherPlayers;

        // Assemble House Hand
        boolean initialBettingRound = json.getBoolean("initialBettingRound");

        // If its not the initial betting round, don't render house cards
        if (!initialBettingRound) {
            int size = json.getInt("houseHandSize"); // Meant to limit loop
            JsonObject houseHand = json.getJsonObject("houseHand");
            Hand hand = new Hand();
            for (int i = 1; i < size; i++) {
                JsonObject houseCard = houseHand.getJsonObject("card" + i);
                Suit cardSuit = Suit.getByName(houseCard.getJsonString("suit").getString());
                Rank cardRank = Rank.getByVal(houseCard.getInt("value"));
                Card card = new Card(cardSuit, cardRank, false);
                hand.addCard(card);
            }
            gb.communityCards = hand;
        }
        draw(gb, log);
    }

    private void draw(GameBoard board, GameLog log) {
        console.clear();
        board.draw(console, Constants.ScreenLayout.GAME_BOARD.y, Constants.ScreenLayout.GAME_BOARD.x);
        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);

        console.setCursor(Constants.ScreenLayout.USER_INPUT);
    }

    private void startConsole() {
        try {
            console = new ConsoleHelper();
            console.initialise();
            console.clear();
        } catch (UnsupportedEncodingException ex) {

        } catch (InterruptedException ex) {

        } catch (IOException ex) {

        }
    }


}
