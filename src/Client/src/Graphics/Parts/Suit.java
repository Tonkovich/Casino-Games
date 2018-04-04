package Graphics.Parts;

public enum Suit {

    CLUBS("♣", "black"),
    DIAMONDS("♦", "red"),
    HEARTS("♥", "red"),
    SPADES("♠", "black");

    public final String symbol;
    public final String color;

    Suit(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

}
