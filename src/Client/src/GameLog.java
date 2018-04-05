import Graphics.ConsoleHelper;
import Graphics.Drawable;
import Graphics.Parts.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Draw messages to the screen within a given box.
 */
public class GameLog implements Drawable {
    // Properties
    //******************************
    /**
     * The messages to display.
     */
    private final List<String> messages;

    /**
     * The maximum number of messages to display at once.
     */
    private final int maxLines;

    /**
     * The number of characters used to draw the top border.
     */
    private final int TOP_BORDER_HEIGHT = 2;

    /**
     * The number of characters used to draw a border.
     */
    private final int SIDE_BORDER_WIDTH = 2;

    /**
     * The maximum text width.
     */
    private final int MAX_TEXT_WIDTH = Constants.ScreenLayout.GAME_LOG_WIDTH - SIDE_BORDER_WIDTH * 2;


    // Public methods
    //******************************

    /**
     * Construct an instance.
     *
     * @param maxLines The maximum number of messages to display at once.
     */
    public GameLog(int maxLines) {
        this.maxLines = maxLines;
        this.messages = new ArrayList<>();
    }

    /**
     * Add a message to the log.
     *
     * @param message The message to add.
     */
    public void add(String message) {
        // TODO: ideally method should handle newlines and wrap long lines. (Not my comment: Creators)

        // truncate if needed
        if (message.length() > MAX_TEXT_WIDTH)
            message = message.substring(0, MAX_TEXT_WIDTH);

        // add message
        messages.add(message);

        // scroll log
        while (messages.size() > maxLines)
            messages.remove(0);
    }

    /**
     * Clear the log.
     */
    public void clear() {
        messages.clear();
    }

    /**
     * Draw the element at the specified position.
     *
     * @param console The console to which to draw.
     * @param row     The top row position.
     * @param col     The left column position.
     */
    public void draw(ConsoleHelper console, int row, int col) {
        console.setCursor(row, col);

        // calculate draw values
        int topOffset = 0;

        // top border
        console.out.print(ConsoleHelper.SET_GRAY_COLOR + "┌─");
        for (int i = 0; i < MAX_TEXT_WIDTH + SIDE_BORDER_WIDTH; i++)
            console.out.print("─");
        topOffset++;

        // messages
        for (int i = 0, lineCount = messages.size(); i < lineCount; i++) {
            String line = messages.get(i);

            // left border
            console.setCursor(row + topOffset, col);
            console.out.print(ConsoleHelper.SET_GRAY_COLOR + "│ " + ConsoleHelper.RESET_COLOR);

            // text
            console.out.print(line);

            topOffset++;
        }

        // blank lines
        for (int i = 0, len = maxLines - messages.size(); i < len; i++) {
            // left border
            console.setCursor(row + topOffset, col);
            console.out.print(ConsoleHelper.SET_GRAY_COLOR + "│ " + ConsoleHelper.RESET_COLOR);

            topOffset++;
        }
    }
}
