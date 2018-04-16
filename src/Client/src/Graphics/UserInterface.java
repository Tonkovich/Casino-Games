package Graphics;

import Graphics.Parts.*;
import Models.Player;

import javax.json.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    public void start(JsonObject json) {
        try {
            console = new ConsoleHelper();
            console.initialise();
            console.clear();

            // Welcome message
            log.add("Welcome to Texas Hold'em heads-up tournament style! We'll be");
            log.add("playing by standard rules. ");

            Hand blankHand = new Hand();
            blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
            blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
            p.hand = blankHand;

            draw(gb, log);

        } catch (UnsupportedEncodingException ex) {

        } catch (InterruptedException ex) {

        } catch (IOException ex) {

        }
    }

    public void update(JsonObject json) {
        // TODO: Pick apart json object, load into board/log, and call draw

    }

    private void draw(GameBoard board, GameLog log) {
        console.clear();
        board.draw(console, Constants.ScreenLayout.GAME_BOARD.y, Constants.ScreenLayout.GAME_BOARD.x);
        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);

        console.setCursor(Constants.ScreenLayout.USER_INPUT);
    }


}
