package util;

import chess.*;
import chess.pieces.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * Class to return instances of anything needed
 */
public class Factory {

    public static ChessGame getNewGame() {
        return new Game();
    }

    public static ChessGame getNewGame(String str) {
        return new Game(str);
    }

    public static ChessBoard getNewBoard() {
        return new Board();
    }

    public static ChessBoard getNewBoard(ChessBoard board) {
        return new Board(board);
    }

    public static ChessBoard getNewBoard(String str) {
        return new Board(str);
    }

    public static ChessPiece getNewPiece(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        if (type == null) return null;
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case ROOK -> new Rook(color);
            case KNIGHT -> new Knight(color);
            case BISHOP -> new Bishop(color);
            case PAWN -> new Pawn(color);
        };
    }

    public static ChessPosition getNewPosition(int row, int col) {
        return new Position(row, col);
    }

    public static ChessPosition getNewPosition(String str) {
        return new Position(str);
    }

    public static ChessMove getNewMove(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotion) {
        return new Move(start, end, promotion);
    }

    public static ChessMove getNewMove(String str) {
        return new Move(str);
    }

    public static Collection<ChessMove> getMoveCollection() {
        return new HashSet<>();
    }
}
