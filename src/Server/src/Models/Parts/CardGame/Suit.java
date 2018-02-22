package Models.Parts.CardGame;

public enum Suit {
    HEARTS("Hearts"),
    SPADES("Spades"),
    CLUBS("Clubs"),
    DIAMONDS("Diamonds");

    private String suitValue;

    Suit(String value) {
        this.suitValue = value;
    }

    public String getVal() {
        return suitValue;
    }

    public int getInt() {
        int num = 0;
        switch (suitValue) {
            case "Hearts":
                num = 1;
                break;
            case "Spades":
                num = 2;
                break;
            case "Clubs":
                num = 3;
                break;
            case "Diamonds":
                num = 4;
                break;
        }
        return num;
    }
}
