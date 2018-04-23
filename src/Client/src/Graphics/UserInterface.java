package Graphics;

import Graphics.Parts.*;
import Models.OtherPlayer;
import Models.Player;
import Utils.ClientSocket;
import Utils.JSONMesssages.PokerActionMessage;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInterface {

    private static UserInterface instance;
    private ConsoleHelper console;
    private GameLog log = new GameLog(5);
    private Player p = Player.getInstance();
    private GameBoard gb = new GameBoard();
    private PokerActionMessage pm = new PokerActionMessage();
    private ClientSocket cs = ClientSocket.getInstance();
    public int gameID;

    private UserInterface() {
    }

    public static UserInterface getInstance() {
        if (instance == null) {
            instance = new UserInterface();
        }
        return instance;
    }

    /**
     * Updates the entire GUI for any possible changes
     *
     * @param json GUI data
     */
    public void update(JsonObject json) {
        // If update is called and client i
        if (console == null) {
            startConsole(); // If client is just joining, start first, then update
            log.add("Welcome to Texas Hold'em! Standard rules apply.");
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

        p.hand = new Hand(card1, card2);

        // Assemble other players
        JsonArray playerIDs = json.getJsonArray("playerIDs");

        Map<Integer, OtherPlayer> otherPlayers = new HashMap<>();

        for (int i = 0; i < playerIDs.size(); i++) {
            int j = playerIDs.getJsonNumber(i).intValue();
            if (j != p.getUserID()) {
                otherPlayers.put(j, new OtherPlayer());
                otherPlayers.get(j).setUserID(j); // Assign userID

                Hand blankHand = new Hand();
                blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
                blankHand.addCard(new Card(Suit.CLUBS, Rank.A, true));
                otherPlayers.get(j).hand = blankHand;
            }
        }
        for (OtherPlayer p : otherPlayers.values()) {
            System.out.println(p.getUsername() + " ");
        }


        // Assemble their bets
        JsonObject allBets = json.getJsonObject("playerBets");
        for (OtherPlayer op : otherPlayers.values()) {
            op.setCurrentBet(allBets.getJsonNumber("player" + op.getUserID()).doubleValue());
        }
        p.setCurrentBet(allBets.getJsonNumber("player" + p.getUserID()).doubleValue());



        // Assemble their names
        JsonObject allNames = json.getJsonObject("allUsernames");
        for (OtherPlayer op : otherPlayers.values()) {
            op.setUsername(allNames.getJsonString("name" + op.getUserID()).getString());
        }
        p.setUsername(allNames.getJsonString("name" + p.getUserID()).getString());



        // Assemble their wallets
        JsonObject allWallets = json.getJsonObject("allWallets");
        for (OtherPlayer op : otherPlayers.values()) {
            op.setPlayerWallet(allWallets.getJsonNumber("wallet" + op.getUserID()).doubleValue());
        }
        p.setPlayerWallet(allWallets.getJsonNumber("wallet" + p.getUserID()).doubleValue());

        gb.otherPlayers = new ArrayList<>(otherPlayers.values());

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

        // Final card flop at end of game
        // TODO: Not secure and client probably shouldn't see this but crunched on time weeeeeeeeeeeeeeeeeeeeeeeeee

        boolean gameDone = json.getBoolean("gameDone");
        if (gameDone) {
            JsonObject allCards = json.getJsonObject("otherPlayersHands");
            for (int i = 0; i < playerIDs.size(); i++) {
                int j = playerIDs.getJsonNumber(i).intValue();
                if (j != p.getUserID()) {
                    Hand newHand = new Hand();

                    JsonObject incomingHand = allCards.getJsonObject("player" + j);

                    JsonObject newCard1 = incomingHand.getJsonObject("card1");
                    Suit newSuit1 = Suit.getByName(newCard1.getString("suit"));
                    Rank newRank1 = Rank.getByVal(newCard1.getInt("value"));

                    JsonObject newCard2 = incomingHand.getJsonObject("card2");
                    Suit newSuit2 = Suit.getByName(newCard2.getString("suit"));
                    Rank newRank2 = Rank.getByVal(newCard2.getInt("value"));

                    newHand.addCard(new Card(newSuit1, newRank1, false));
                    newHand.addCard(new Card(newSuit2, newRank2, false));


                    otherPlayers.get(j).setHand(newHand);
                }
            }
        }

        draw(gb, log);
    }

    public void getInput(JsonObject json) {
        boolean otherUserBet = json.getBoolean("otherUserBet");
        boolean newGame = json.getBoolean("newGame");
        checkInput(json, otherUserBet, newGame);
    }


    private void checkInput(JsonObject json, boolean otherUserBet, boolean newGame) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        String newMessage = json.getString("pokerAction");
        log.add(newMessage);

        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);

        console.setCursor(Constants.ScreenLayout.USER_INPUT);
        console.clearFromCursorLine();
        console.out.print("> ");
        String result = "";
        try {
            result = br.readLine();
        } catch (IOException ex) {

        }

        if ((otherUserBet || !otherUserBet) && !newGame) {
            if (result.trim().equalsIgnoreCase("f")) {
                cs.sendMessage(pm.fold(gameID, p.getUserID()));
            } else if (result.trim().equalsIgnoreCase("c") && otherUserBet) {
                cs.sendMessage(pm.call(gameID, p.getUserID()));

            } else if (result.trim().equalsIgnoreCase("ch") && !otherUserBet) {
                cs.sendMessage(pm.check(gameID, p.getUserID()));

            } else if (result.trim().equalsIgnoreCase("b") && !otherUserBet) {
                // Ask for amount and add to prevBet
                log.add("How much?");
                log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                console.clearFromCursorLine();
                console.out.print("> ");
                String amount = "";
                try {
                    amount = br.readLine();
                } catch (IOException ex) {

                }

                try {
                    double bet = Double.parseDouble(amount);
                    boolean insufficientFunds = bet > p.getPlayerWallet();
                    if (bet < 0 || bet == 0 || insufficientFunds) {
                        log.add("Incorrect Number: Try again:                        ");

                        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                        checkInput(json, otherUserBet, newGame);
                    } else {
                        // TODO If raise == wallet, send all in message instead
                        cs.sendMessage(pm.bet(gameID, p.getUserID(), bet));
                    }
                } catch (NumberFormatException ex) {
                    log.add("Incorrect Number: Try again                             ");
                    log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                    checkInput(json, otherUserBet, newGame);
                }
            } else if (result.trim().equalsIgnoreCase("r")) {
                // Ask for amount and add to prevBet
                log.add("How much?");
                log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                console.clearFromCursorLine();
                console.out.print("> ");
                String amount = "";
                try {
                    amount = br.readLine();
                } catch (IOException ex) {

                }

                try {
                    double raise = Double.parseDouble(amount);
                    boolean insufficientFunds = raise > p.getPlayerWallet();
                    if (raise < 0 || raise == 0 || insufficientFunds) {
                        log.add("Incorrect Number: Try again");
                        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                        checkInput(json, otherUserBet, newGame);
                    } else {
                        // TODO If raise == wallet, send all in message instead
                        cs.sendMessage(pm.raise(gameID, p.getUserID(), raise));
                    }
                } catch (NumberFormatException ex) {
                    log.add("Incorrect Number: Try again                            ");
                    log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                    checkInput(json, otherUserBet, newGame);
                }
            } else {
                // Incorrect input, try again
                log.add("Incorrect choice: Try again                            ");
                log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
                checkInput(json, otherUserBet, newGame);
            }
            console.clearFromCursorLine();
            console.out.print("> ");
        } else {
            if (result.trim().equalsIgnoreCase("y")) {
                cs.sendMessage(pm.readyUp(gameID, p.getUserID(), true));
            } else if (result.trim().equalsIgnoreCase("n")) {
                cs.sendMessage(pm.readyUp(gameID, p.getUserID(), false));
            } else {
                log.add("Incorrect choice: Try again");
                checkInput(json, otherUserBet, newGame);
            }
        }
    }



    private void draw(GameBoard board, GameLog log) {
        console.clear();
        board.draw(console, Constants.ScreenLayout.GAME_BOARD.y, Constants.ScreenLayout.GAME_BOARD.x);
        log.draw(console, Constants.ScreenLayout.GAME_LOG.y, Constants.ScreenLayout.GAME_LOG.x);
        console.setCursor(Constants.ScreenLayout.USER_INPUT);
    }

    public void updateLog(JsonObject json) {
        if (console == null) {
            startConsole(); // If client is just joining, start first, then update
            log.add("Welcome to Texas Hold'em! Standard rules apply.");
        }
        String newMessage = json.getString("pokerMessage");
        log.add(newMessage);
        draw(gb, log);
    }

    public void exitGame() {
        console.killProcess();
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
