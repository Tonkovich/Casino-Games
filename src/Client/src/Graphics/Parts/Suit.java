package Graphics.Parts;

public enum Suit {

    CLUBS("♣", "black"),
    DIAMONDS("♦", "red"),
    HEARTS("♥", "red"),
    SPADES("♠", "black");

    public String symbol;
    public String color;

    Suit(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

    // Used to translate JSON data into Suit
    public static Suit getByName(String name) {
        Suit suit = CLUBS;
        switch (name) {
            case "Hearts":
                suit = HEARTS;
                break;
            case "Spades":
                suit = SPADES;
                break;
            case "Clubs":
                suit = CLUBS;
                break;
            case "Diamonds":
                suit = DIAMONDS;
                break;
        }
        return suit;
    }

}
