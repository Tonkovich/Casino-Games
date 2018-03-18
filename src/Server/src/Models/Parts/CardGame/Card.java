package Models.Parts.CardGame;

import java.util.Comparator;

public class Card implements Comparator<Card> {
    private Suit suit;
    private CardValue value;
    private boolean isPlayers;

    Card(CardValue value, Suit suit, boolean isPlayers) {
        this.suit = suit;
        this.value = value;
        this.isPlayers = isPlayers;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public CardValue getCardValue() {
        return value;
    }

    public void setCardValue(CardValue value) {
        this.value = value;
    }

    public boolean isPlayers() {
        return isPlayers;
    }

    public void setIsPlayers(boolean isPlayers) {
        this.isPlayers = isPlayers;
    }

    @Override
    public int compare(Card v1, Card v2) {
        int v1Val = v1.getCardValue().getVal();
        int v2Val = v2.getCardValue().getVal();
        return Integer.compare(v1Val, v2Val);
    }

    public String toString() {
        return value + " of " + suit.toString() + ", ";
    }

}
