package Models.Parts.CardGame;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand = new ArrayList<>();

    public void addCard(Card c) {
        hand.add(c);
    }

    // Sounds weird but needed to replace arraylist
    public void addHand(ArrayList<Card> hand) {
        this.hand = hand;
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
            result += c.getCardValue() + " of " + c.getSuit() + ", ";
        }

        return result;
    }

    public Hand addAll(Hand h2) {
        ArrayList<Card> temp = new ArrayList<>(hand);
        for (Card c : h2.getCards()) {
            temp.add(c);
        }
        Hand newHand = new Hand();
        newHand.addHand(temp);
        return newHand;
    }
}
