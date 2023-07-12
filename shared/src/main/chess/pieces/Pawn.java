package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;
import java.util.HashSet;

/**
 * Pawn implementation of the ChessPiece interface
 */
public class Pawn implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type = PieceType.PAWN;

    /**
     * Basic constructor
     * @param color This pawn's team color
     */
    public Pawn(ChessGame.TeamColor color) {
        this.color = color;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = Factory.getMoveCollection();

        int begin = color == ChessGame.TeamColor.WHITE ? 2 : 7;
        int end = color == ChessGame.TeamColor.WHITE ? 7 : 2;

        if (myPosition.getRow() == end) {
            // 1 space, promo only
            addForwardMove(board, myPosition, moves, false, true);
            // Diagonals, promo only
            addDiagonalMoves(board, myPosition, moves, true);
        } else {
            // 1 space, no promo
            boolean oneSquareValid = addForwardMove(board, myPosition, moves, false, false);
            // Diagonals, no promo
            addDiagonalMoves(board, myPosition, moves, false);
            if (myPosition.getRow() == begin && oneSquareValid) {
                addForwardMove(board, myPosition, moves, true, false);
            }
        }

        return moves;
    }

    private boolean addForwardMove(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, boolean two, boolean promo) {

        // When using this method for moving two squares, it is assumed that it can also move one square.
        // It is also assumed that the position is a valid position for a pawn (rows 2 - 7 only)

        if (two && promo) return false; // Can't move 2 squares and be promoted

        int mod = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        if (two) mod *= 2;

        ChessPosition newPos = Factory.getNewPosition(position.getRow() + mod, position.getColumn());
        ChessPiece resident = board.getPiece(newPos);

        if (resident == null) {
            if (promo) for (PieceType type : getPromoPieces()) moves.add(Factory.getNewMove(position, newPos, type));
            else moves.add(Factory.getNewMove(position, newPos, null));
            return true;
        } else return false;
    }

    private void addDiagonalMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, boolean promo) {

        int mod = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        ChessPosition leftPos = Factory.getNewPosition(position.getRow() + mod, position.getColumn() - 1);
        ChessPosition rightPos = Factory.getNewPosition(position.getRow() + mod, position.getColumn() + 1);
        ChessPiece leftResident = null;
        if (Util.validatePosition(leftPos)) leftResident = board.getPiece(leftPos);
        ChessPiece rightResident = null;
        if (Util.validatePosition(rightPos)) rightResident = board.getPiece(rightPos);

        // LEFT
        if (leftResident != null && leftResident.getTeamColor() != color) {
            if (promo) for (PieceType type : getPromoPieces()) moves.add(Factory.getNewMove(position, leftPos, type));
            else moves.add(Factory.getNewMove(position, leftPos, null));
        }

        // RIGHT
        if (rightResident != null && rightResident.getTeamColor() != color) {
            if (promo) for (PieceType type : getPromoPieces()) moves.add(Factory.getNewMove(position, rightPos, type));
            else moves.add(Factory.getNewMove(position, rightPos, null));
        }
    }

    private Collection<ChessPiece.PieceType> getPromoPieces() {
        Collection<ChessPiece.PieceType> types = new HashSet<>();
        types.add(ChessPiece.PieceType.ROOK);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.QUEEN);
        return types;
    }
}
