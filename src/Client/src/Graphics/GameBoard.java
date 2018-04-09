package Graphics;

import Graphics.Parts.*;
import Models.Player;

import java.awt.*;
import java.util.List;

/**
 * Manages and draws the game board (i.e. community cards, chips, and hands).
 */
public class GameBoard implements Drawable {

    public Player human;
    public List<Player> otherPlayers;
    public Hand communityCards = new Hand();
    public int bigBlind = Constants.INITIAL_BIG_BLIND;
    public int smallBlind = Constants.INITIAL_BIG_BLIND / 2;
    public boolean isStarted;


    private Card facedownCard = new Card(Suit.CLUBS, Rank.A, true);

    public GameBoard(Player user, boolean isStarted) {
        this.human = user;
        this.isStarted = isStarted;
    }

    public void draw(ConsoleHelper console, int row, int col) {
        Point origin = new Point(col, row);

        // draw players
        for (Player p : otherPlayers) {
            // TODO adjust constants for spacing of places
            drawPlayer(console, p, origin, Constants.BoardLayout.BOT_NAME_AND_BET, Constants.BoardLayout.BOT_HAND);
        }
        //drawPlayer(console, bot, origin, Constants.BoardLayout.BOT_NAME_AND_BET, Constants.BoardLayout.BOT_HAND);
        drawPlayer(console, human, origin, Constants.BoardLayout.USER_NAME_AND_BET, Constants.BoardLayout.USER_HAND);

        // draw pile

        Point position = getRelativePoint(origin, Constants.BoardLayout.CARD_PILE);
        facedownCard.draw(console, position.y, position.x);
        facedownCard.draw(console, position.y, position.x + 1);     // Draws deck
        facedownCard.draw(console, position.y, position.x + 2);

        // draw community cards
        position = getRelativePoint(origin, Constants.BoardLayout.COMMUNITY_CARDS);
        communityCards.draw(console, position.y, position.x);

        // draw pot & betting info
        //drawInfoBox(console, origin, Constants.BoardLayout.BET_INFO_BOX);
    }


    private Point getRelativePoint(Point origin, Point coordinate) {
        return new Point(origin.x + coordinate.x, origin.y + coordinate.y);
    }


    private void drawPlayer(ConsoleHelper console, Player player, Point origin, Point nameAndBetPos, Point handPos) {
        // draw bot name + bet
        console.setCursor(getRelativePoint(origin, nameAndBetPos));
        //console.out.print(player.name + " bet $" + player.bet);

        // draw bot hand
        Point handPosAbs = getRelativePoint(origin, handPos);
        //player.hand.draw(console, handPosAbs.y, handPosAbs.x);
    }

    /*private void drawInfoBox(ConsoleHelper console, Point origin, Point position) {
        Point boxPos = this.getRelativePoint(origin, position);

        // get text to display
        List<String> lines = Arrays.asList(
                "   Pot: $" + (human.bet + bot.bet),
                "Blinds: $" + (smallBlind) + " – $" + bigBlind,
                " Your chips: $" + human.chips,
                "Their chips: $" + bot.chips
        );
        int innerBorderAt = 3; // TODO  May have to be changed for more players and dynamic

        // get box dimensions
        int textWidth = 0;
        for (String line : lines)
            textWidth = Math.max(textWidth, line.length());

        // draw box
        int firstCol = boxPos.x;
        int firstRow = boxPos.y;
        int curRow = 0;

        // top border
        console.setCursor(firstRow + curRow, firstCol);
        console.out.print(ConsoleHelper.SET_GRAY_COLOR);
        console.out.print(ConsoleHelper.SET_GRAY_COLOR + "┌─");
        for (int i = 0; i < textWidth; i++)
            console.out.print("─");
        console.out.print("─┐");
        console.out.print(ConsoleHelper.RESET_COLOR);
        curRow++;

        // text
        for (String line : lines) {
            // text line
            console.setCursor(firstRow + curRow, firstCol);
            console.out.print(ConsoleHelper.SET_GRAY_COLOR + "│ " + ConsoleHelper.RESET_COLOR + line);
            console.setCursor(firstRow + curRow, firstCol + 2 + textWidth);
            console.out.print(ConsoleHelper.SET_GRAY_COLOR + " │" + ConsoleHelper.RESET_COLOR);
            curRow++;

            // inner border
            if (curRow == innerBorderAt) {
                console.setCursor(firstRow + curRow, firstCol);
                console.out.print(ConsoleHelper.SET_GRAY_COLOR);
                console.out.print("├─");
                for (int i = 0; i < textWidth; i++)
                    console.out.print("─");
                console.out.print("─┤");
                console.out.print(ConsoleHelper.RESET_COLOR);
                curRow++;
            }
        }

        // bottom border
        console.setCursor(firstRow + curRow, firstCol);
        console.out.print(ConsoleHelper.SET_GRAY_COLOR);
        console.out.print("└─");
        for (int i = 0; i < textWidth; i++)
            console.out.print("─");
        console.out.print("─┘");
        console.out.print(ConsoleHelper.RESET_COLOR);
    }*/
}
