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
    Suit(String name) {
        switch (name) {
            case "Hearts":
                this.symbol = "♥";
                this.color = "red";
                break;
            case "Spades":
                this.symbol = "♠";
                this.color = "black";
                break;
            case "Clubs":
                this.symbol = "♣";
                this.color = "black";
                break;
            case "Diamonds":
                this.symbol = "♦";
                this.color = "red";
                break;
        }
    }

}
