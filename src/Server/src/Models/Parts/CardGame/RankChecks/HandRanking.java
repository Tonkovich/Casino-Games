package Models.Parts.CardGame.RankChecks;

public enum HandRanking {
    HIGH_CARD(0),
    ONE_PAIR(1),
    TWO_PAIR(2),
    THREE_OAK(3),
    STRAIGHT(4),
    ROYAL_STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    FOUR_OAK(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLUSH(10);

    private int handRanking;

    HandRanking(int value) {
        this.handRanking = value;
    }

    public int getVal() {
        return handRanking;
    }
}
