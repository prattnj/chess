package chess;

import java.util.Objects;

/**
 * Implementation of the ChessPosition interface
 */
public class Position implements ChessPosition {

    private final int row;
    private final int col;

    /**
     * Basic constructor
     * @param row The vertical component of this position (1-8 in chess notation)
     * @param col The horizontal component of this position (a-h in chess notation)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Initializes this as a deep copy of an already existing position
     * @param str The string representation of the position to create a deep copy of (must be formatted like this.toString())
     */
    public Position(String str) {
        // WARNING: Assumes that str is formatted like this.toString()
        col = str.charAt(0) - 'a' + 1;
        row = Integer.parseInt(String.valueOf(str.charAt(1)));
    }

    @Override
    public Integer getRow() {
        return row;
    }

    @Override
    public Integer getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        char letter = (char) ('a' + col - 1);
        return letter + String.valueOf(row);
    }
}
