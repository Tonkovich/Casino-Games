package Models.Parts.CardGame.RankChecks;

import Models.Parts.CardGame.Card;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card v1, Card v2) {
        int v1Val = v1.getCardValue().getVal();
        int v2Val = v2.getCardValue().getVal();
        return Integer.compare(v1Val, v2Val);
    }
}
