package Graphics.Parts;

import Graphics.ConsoleHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {

    private ArrayList<Card> cards = new ArrayList<>();

    public Hand() {

    }

    public Hand(Card... c) {
        cards.addAll(Arrays.asList(c));
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void addCards(Card... c) {
        cards.addAll(Arrays.asList(c));
    }

    public void draw(ConsoleHelper console, int row, int col) {
        int leftOffset = 0;
        for (int i = 0, len = cards.size(); i < len; i++) {
            cards.get(i).draw(console, row, col + leftOffset);
            leftOffset += Constants.CARD_LAYOUT_WIDTH + Constants.CARD_BORDER_WIDTH * 2;
        }
    }

    public int getSize() {
        return cards.size();
    }
}
