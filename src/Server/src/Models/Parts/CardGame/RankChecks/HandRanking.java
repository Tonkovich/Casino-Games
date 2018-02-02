package Models.Parts.CardGame.RankChecks;

public enum HandRanking {
    /*
     * Increments of 15 to account for high card, (i.e) 14 for ace
     */
    HIGH_CARD(0),
    ONE_PAIR(15),
    TWO_PAIR(30),
    THREE_OAK(45),
    STRAIGHT(60),
    FLUSH(75),
    FULL_HOUSE(90),
    FOUR_OAK(105),
    STRAIGHT_FLUSH(120),
    ROYAL_FLUSH(135);

    private int handRanking;

    HandRanking(int value) {
        this.handRanking = value;
    }

    public int getHandRanking() {
        return handRanking;
    }
}
