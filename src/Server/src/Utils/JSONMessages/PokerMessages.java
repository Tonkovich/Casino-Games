package Utils.JSONMessages;

import Models.Games.Player;
import Models.Parts.CardGame.Card;

import javax.json.Json;
import javax.json.JsonObject;

public class PokerMessages implements CardGameMessages {

    public String winnerMessageOthers(Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", player.getUsername() + " won!").build();
        return json.toString();
    }

    public String winnerMessage() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "You won!").build();
        return json.toString();
    }

    public String gameStarted() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "Game has started").build();
        return json.toString();
    }

    public String waiting() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "Waiting...").build();
        return json.toString();
    }

    public String addedToGame(Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", player.getUsername() + " joined the game").build();
        return json.toString();
    }

    public String cardDrawn(Card c) {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", c.getCardValue().getVal() + " of " + c.getSuit().getVal()).build();
        return json.toString();
    }

    public String moveNotAllowed() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "Move not allowed").build();
        return json.toString();
    }

    public String gameReady() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "Game ready!")
                .add("message2", "Game starting...").build();
        return json.toString();
    }

    public String addedToPot(double amount, double pot, Player player) {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", player.getUsername() + " added " + amount
                        + " to the pot, Current: " + pot).build();
        return json.toString();
    }

    public String gameCompleted() {
        JsonObject json = Json.createObjectBuilder()
                .add("header", "serverMessage")
                .add("message", "Round over: Play again(y/n)").build();
        return json.toString();
    }
}
