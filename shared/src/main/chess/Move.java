package chess;

import util.Factory;

import java.util.Objects;

/**
 * Implementation of the ChessMove interface
 */
public class Move implements ChessMove {

    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotion;

    /**
     * Basic constructor
     * @param start The starting position of this move
     * @param end The ending position of this move
     * @param promotion The piece type for pawn promotion when applicable, otherwise null
     */
    public Move(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotion) {
        this.start = start;
        this.end = end;
        this.promotion = promotion;
    }

    /**
     * Initializes this as a deep copy of an already existing move
     * @param str The string representation of the move to create a deep copy of (must be formatted like this.toString())
     */
    public Move(String str) {
        // WARNING: Assumes that str is formatted like this.toString()
        start = Factory.getNewPosition(str.substring(0, 2));
        end = Factory.getNewPosition(str.substring(2, 4));
        if (str.length() > 4) {
            promotion = switch (str.charAt(4)) {
                case 'Q' -> ChessPiece.PieceType.QUEEN;
                case 'R' -> ChessPiece.PieceType.ROOK;
                case 'N' -> ChessPiece.PieceType.KNIGHT;
                case 'B' -> ChessPiece.PieceType.BISHOP;
                default -> null;
            };
        } else promotion = null;
    }

    @Override
    public ChessPosition getStartPosition() {
        return start;
    }

    @Override
    public ChessPosition getEndPosition() {
        return end;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(start, move.start) && Objects.equals(end, move.end) && promotion == move.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotion);
    }

    @Override
    public String toString() {
        String promo = promotion == null ? "" : switch (promotion) {
            case QUEEN -> "Q";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case PAWN -> "P";
            default -> "";
        };
        return start.toString() + end.toString() + promo;
    }
}
