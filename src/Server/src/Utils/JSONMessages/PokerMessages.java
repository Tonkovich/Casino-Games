package Utils.JSONMessages;

import Models.Games.Player;
import Models.Parts.CardGame.Card;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;

public class PokerMessages implements CardGameMessages {

    public String winnerMessageOthers(Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", player.getUsername() + " won!").build();
        return json.toString();
    }

    public String multipleWinners(List<Player> player) {
        String allPlayers = "";
        for (Player p : player) {
            allPlayers += p.getUsername() + ", ";
        }
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", allPlayers + " won!").build();
        return json.toString();
    }

    public String winnerMessage() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", "You won!").build();
        return json.toString();
    }

    public String gameStarted() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", "Game has started").build();
        return json.toString();
    }

    public String waiting() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", "Waiting...").build();
        return json.toString();
    }

    public String addedToGame(Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", player.getUsername() + " joined the game").build();
        return json.toString();
    }

    public String cardDrawn(Card c) {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", c.getCardValue().getVal() + " of " + c.getSuit().getVal()).build();
        return json.toString();
    }

//    public String moveNotAllowed() {
//        JsonObject json = Json.createObjectBuilder()
//                .add("pokerMessage", "Move not allowed").build();
//        return json.toString();
//    }

    public String gameReady() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", "Game ready!\nGame starting...").build();
        return json.toString();
    }

    public String addedToPot(double amount, double pot, Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", player.getUsername() + " added " + amount
                        + " to the pot, Current: " + pot).build();
        return json.toString();
    }

    public String gameCompleted() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerAction", "Round over: Play again(y/n)").build();
        return json.toString();
    }

    public String userFold(String name) {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerMessage", name + " folds.").build();
        return json.toString();
    }

    public String betWithCheck() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerAction", "Do you want to (f)old or (ch)eck or (r)aise")
                .add("otherUserBet", false).build();
        return json.toString();
    }

    public String betWithCall() {
        JsonObject json = Json.createObjectBuilder()
                .add("pokerAction", "Do you want to (f)old or (c)all or (r)aise")
                .add("otherUserBet", true).build();
        return json.toString();
    }
}
