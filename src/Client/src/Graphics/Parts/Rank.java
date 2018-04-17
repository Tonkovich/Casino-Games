package Graphics.Parts;

public enum Rank {
    // Constants
    //******************************
    A(1, "A", "A    \n     \n  %  \n     \n    A"),
    TWO(2, "2", "2    \n  %  \n     \n  %  \n    2"),
    THREE(3, "3", "3    \n  %  \n  %  \n  %  \n    3"),
    FOUR(4, "4", "4    \n%   %\n     \n%   %\n    4"),
    FIVE(5, "5", "5    \n%   %\n  %  \n%   %\n    5"),
    SIX(6, "6", "6    \n%   %\n%   %\n%   %\n    6"),
    SEVEN(7, "7", "7    \n%   %\n% % %\n%   %\n    7"),
    EIGHT(8, "8", "8    \n% % %\n%   %\n% % %\n    8"),
    NINE(9, "9", "9    \n% % %\n% % %\n% % %\n    9"),
    TEN(10, "10", "10  %\n% % %\n% % %\n% % %\n   10"),
    JACK(11, "J", "J    \n%    \nJACK \n    %\n    J"),
    QUEEN(12, "Q", "Q    \n%    \nQUEEN\n    %\n    Q"),
    KING(13, "K", "K    \n%    \nKING \n    %\n    K");


    // Properties
    //******************************
    /**
     * The card value when choosing a high card.
     */
    public final int value;

    /**
     * The display symbol.
     */
    public final String symbol;

    /**
     * The card layout on a five-by-five grid, where % represents the suit symbol.
     */
    public final String layout;


    // Public methods
    //******************************

    /**
     * Construct an instance.
     *
     * @param value  The card value when choosing a high card.
     * @param symbol The display symbol.
     * @param layout The card layout on a five-by-five grid, where % represents the suit symbol.
     */
    Rank(int value, String symbol, String layout) {
        this.value = value;
        this.symbol = symbol;
        this.layout = layout;
    }

    /**
     * Get whether the given ranks are consecutive in the given order.
     *
     * @param ranks The ranks to check.
     */
    public static boolean areConsecutive(Rank[] ranks) {
        for (int i = 0; i < ranks.length - 1; i++) {
            if (Math.abs(ranks[i].value - ranks[i + 1].value) != 1)
                return false;
        }
        return true;
    }

    /**
     * Used for getting enum rank by value.
     * i.e card val send from server
     *
     * @param val Incoming card value
     * @return the correct enum rank
     */
    public static Rank getByVal(int val) {
        Rank rank = A;
        switch (val) {
            case 1:
                rank = A;
                break;
            case 2:
                rank = TWO;
                break;
            case 3:
                rank = THREE;
                break;
            case 4:
                rank = FOUR;
                break;
            case 5:
                rank = FIVE;
                break;
            case 6:
                rank = SIX;
                break;
            case 7:
                rank = SEVEN;
                break;
            case 8:
                rank = EIGHT;
                break;
            case 9:
                rank = NINE;
                break;
            case 10:
                rank = TEN;
                break;
            case 11:
                rank = JACK;
                break;
            case 12:
                rank = QUEEN;
                break;
            case 13:
                rank = KING;
                break;
        }
        return rank;
    }
}
