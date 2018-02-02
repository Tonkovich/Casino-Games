package Models.Parts.CardGame.RankChecks;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.CardValue;
import Models.Parts.CardGame.Hand;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreHand {

    Hand hand;
    HandRanking rank;
    Card highCard;

    public ScoreHand(Hand playerHand, Hand houseHand) {
        hand = playerHand.addAll(houseHand);
        Collections.sort(hand.getCards(), new CardComparator());
    }

    private void rankHand() {

    }


    public boolean isInAStraight() {
        List<Integer> sortedValues = hand.getCards().stream().map(Card::getCardValue).map(CardValue::getVal)
                .sorted(Collections.reverseOrder()).collect(Collectors.toList());

        int sum = 0;
        int currentValue = sortedValues.get(0);
        for (int value : sortedValues) {
            sum += currentValue - value;
            currentValue = value;
        }
        return sum == 4;
    }

    public boolean isAllOfTheSameSuit() {
        return hand.getCards().stream().collect(Collectors.groupingBy(Card::getSuit)).size() == 1;
    }
}
