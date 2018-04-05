package Graphics.Parts;

import Graphics.ConsoleHelper;
import Graphics.Drawable;

public class Card implements Drawable {

    private final Rank rank;

    private final Suit suit;

    private final boolean isFaceDown;

    public Card(Suit suit, Rank rank, boolean isFaceDown) {
        this.suit = suit;
        this.rank = rank;
        this.isFaceDown = isFaceDown;
    }

    public void draw(ConsoleHelper console, int row, int col) {
        // top border
        console.setCursor(row, col);
        console.out.print("┌─");
        for (int i = 0; i < Constants.CARD_LAYOUT_WIDTH; i++)
            console.out.print("─");
        console.out.print("─┐");

        // card layout
        if (isFaceDown) {
            // TODO: Replicate more for many players
            for (int layoutRow = 0; layoutRow < Constants.CARD_LAYOUT_HEIGHT; layoutRow++) {
                console.setCursor(row + 1 + layoutRow, col);
                console.out.print("│ ");
                for (int layoutCol = 0; layoutCol < Constants.CARD_LAYOUT_WIDTH; layoutCol++)
                    console.out.print("▒");
                console.out.print(" │");
            }
        } else {
            String layout = "│ " + rank.layout.replace("%", suit.symbol).replace("\n", " │\n│ ") + " │";
            String[] lines = layout.split("\n");
            for (int i = 0; i < lines.length; i++) {
                console.setCursor(row + i + 1, col);
                console.out.print(lines[i]);
            }
        }

        // bottom border
        console.setCursor(row + Constants.CARD_LAYOUT_HEIGHT + 1, col);
        console.out.print("└─");
        for (int i = 0; i < Constants.CARD_LAYOUT_WIDTH; i++)
            console.out.print("─");
        console.out.print("─┘");
    }
}
