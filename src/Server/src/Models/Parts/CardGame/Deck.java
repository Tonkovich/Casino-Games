package Models.Parts.CardGame;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> deck;

    // Calling this constructor will automatically load a deck and shuffle it
    public Deck() {
        deck = new ArrayList<>();
        for (CardValue v : CardValue.values()) {
            for (Suit s : Suit.values()) {
                Card card = new Card(v, s);
                deck.add(card);
            }
        }
        Collections.shuffle(deck);
    }

    // Shuffle method just in case
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public ArrayList<Card> getCards() {
        return deck;
    }

    public Card drawCard() {
        return deck.remove(0);
        // TODO: Check if deck is empty
    }

    public int size() {
        return deck.size();
    }

    public String toString() {
        String result = "";

        for (Card c : deck) {
            result += c.getCardValue() + " of " + c.getSuit() + " \n";
        }

        return result;
    }

}
