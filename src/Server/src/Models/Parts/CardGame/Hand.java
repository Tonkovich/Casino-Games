package Models.Parts.CardGame;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;

    public void addCard(Card c) {
        hand.add(c);
    }

    public ArrayList<Card> getCards() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }

    // Used for message sending to client
    public String toString() {
        String result = "";

        for (Card c : hand) {
            result += c.getCardValue() + " of " + c.getSuit() + " ";
        }

        return result;
    }

    public Hand addAll(Hand h2) {
        for (Card c : h2.getCards()) {
            addCard(c);
        }
        return this;
    }
}
