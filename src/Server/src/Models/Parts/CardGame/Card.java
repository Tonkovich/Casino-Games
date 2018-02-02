package Models.Parts.CardGame;

import java.util.Comparator;

public class Card implements Comparator<Card> {
    private Suit suit;
    private CardValue value;

    Card(CardValue value, Suit suit) {
        this.suit = suit;
        this.value = value;
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

    @Override
    public int compare(Card v1, Card v2) {
        int v1Val = v1.getCardValue().getVal();
        int v2Val = v2.getCardValue().getVal();
        return Integer.compare(v1Val, v2Val);
    }

}
