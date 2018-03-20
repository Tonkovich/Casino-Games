package Models.Parts.CardGame.Ranking;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.CardValue;
import Models.Parts.CardGame.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluateHand {

    // constants for evaluating pairs
    private static final int ONE_PAIR = 4;
    private static final int TWO_PAIR = 8;
    private static final int THREE_OF_A_KIND = 9;
    private static final int FULL_HOUSE = 13;
    private static final int FOUR_OF_A_KIND = 16;

    private Hand hand;
    private int[] faceFrequency = new int[15];
    private int[] suitFrequency = new int[5];
    private boolean hasAce;
    private boolean isRoyal;
    private HandRanking rank;
    private Card highCard;
    private Hand playerHand;


    public EvaluateHand(Hand playerHand, Hand houseHand) {
        this.playerHand = playerHand; // Im lazy so im using this for getPairSum()
        hand = playerHand.addAll(houseHand);
        Collections.sort(hand.getCards(), new Card()); // Passes cards to comparator and sorts
        getFrequencies();
        rank = HandRanking.HIGH_CARD;
        findHighCard();
        rankHand(); // Will replace highcard if other hand found
    }

    public int getRank() {
        return rank.getVal();
    }

    public int getHighCard() {
        return highCard.getCardValue().getVal();
    }

    private void rankHand() {

        // find all possibilities of straights first
        if (isStraight()) {
            if (isFlush()) {
                rank = isRoyal ? HandRanking.ROYAL_FLUSH : HandRanking.STRAIGHT_FLUSH;
            } else {
                rank = isRoyal ? HandRanking.ROYAL_STRAIGHT : HandRanking.STRAIGHT;
            }
        } else {
            if (isFlush()) rank = HandRanking.FLUSH;
        }

        // now find pairs/other multiples
        int pairs = getPairSum();
        switch (pairs) {
            case ONE_PAIR:
                if (rank.compareTo(HandRanking.ONE_PAIR) < 0)
                    rank = HandRanking.ONE_PAIR;
                break;
            case TWO_PAIR:
                if (rank.compareTo(HandRanking.TWO_PAIR) < 0)
                    rank = HandRanking.TWO_PAIR;
                break;
            case THREE_OF_A_KIND:
                if (rank.compareTo(HandRanking.THREE_OAK) < 0)
                    rank = HandRanking.THREE_OAK;
                break;
            case FULL_HOUSE:
                if (rank.compareTo(HandRanking.FULL_HOUSE) < 0)
                    rank = HandRanking.FULL_HOUSE;
                break;
            case FOUR_OF_A_KIND:
                if (rank.compareTo(HandRanking.FOUR_OAK) < 0)
                    rank = HandRanking.FOUR_OAK;
                break;
            default:
                rank = HandRanking.HIGH_CARD; // Don't have anything
        }
    }

    private void getFrequencies() {
        for (Card c : hand.getCards()) {
            if (c.getCardValue().getVal() == 14)
                hasAce = true;
            faceFrequency[c.getCardValue().getVal()]++;
            suitFrequency[c.getSuit().getInt()]++;
        }

    }


    private boolean isFlush() {
        ArrayList<Card> pc = playerHand.getCards();
        int index = 0;
        for (int each : suitFrequency) {
            if (each == 5) {
                if (pc.get(0).getSuit().getInt() == index || pc.get(1).getSuit().getInt() == index) {
                    return true;
                }
            }
            index++;
        }
        return false;
    }


    private boolean isStraight() {
        List<Integer> sorted = hand.getCards()
                .stream()
                .map(Card::getCardValue)
                .map(CardValue::getVal)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList()); // I hope this shit works lol

        // Create sorted values of isPlayers for later checking (Texas Holdem)
        List<Boolean> bools = new ArrayList<>();
        for (Card c : hand.getCards()) {
            int val = c.getCardValue().getVal();
            int counter = 0;
            // Find card with same value, then register isPlayers()
            bools.add(counter, false);
            for (Integer i : sorted) {
                if (i == val) {
                    bools.add(counter, c.isPlayers());
                }
            }
            counter++;
        }


        int sum = 0;
        boolean belongs = false;
        // A, K, Q, J, 10 is a royal flush
        if (hasAce && sorted.get(5) == 13 && sorted.get(4) == 12 && sorted.get(3) == 11 && sorted.get(2) == 10) {
            // You must own either one of the card or have a 9 before 10
            if ((bools.get(6) || bools.get(5) || bools.get(4) || bools.get(3) || bools.get(2)) || (sorted.get(1) == 9 && bools.get(1))) {
                isRoyal = true;
                belongs = true;
                sum = 5; // Just to return straight true as well
            }
        } else {
            for (int i = 1; i < sorted.size(); i++) {
                // 6-5 = 1, way to detect if one after another
                if (sorted.get(i) - sorted.get(i - 1) == 1) {
                    if (bools.get(i) || bools.get(i - 1))
                        belongs = true;
                    sum++;
                }
            }
        }

        // Must have at least 5 in a row and own at least one of the cards
        return (sum >= 5 && belongs);
    }

    private int getPairSum() {
        ArrayList<Card> playerCards = playerHand.getCards();
        boolean belongs = false;
        int sum = 0;
        int index = 0;

        // We must find out if they have a card in the river to be able to qualify
        for (int each : faceFrequency) {
            if (each > 1) {
                // n-pair is detected, very ugly, check
                if (playerCards.get(0).getCardValue().getVal() == index || playerCards.get(1).getCardValue().getVal() == index) {
                    belongs = true;
                }
            }
            index++;
        }
        index = 0;
        // If user belongs, time to tally up the pairs
        if (belongs) {
            for (int each : faceFrequency) {
                if (each > 1) {
                    // n-pair is detected, very ugly, check
                    sum += each * each;
                }
                index++;
            }
        }
        return sum;
    }

    private void findHighCard() {
        ArrayList<Card> temp = playerHand.getCards();
        if (temp.get(0).getCardValue().getVal() > temp.get(1).getCardValue().getVal()) {
            highCard = temp.get(0);
        } else {
            highCard = temp.get(1);
        }
    }

}
