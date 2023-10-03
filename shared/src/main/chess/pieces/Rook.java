package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * Rook implementation of the ChessPiece interface
 */
public class Rook extends Piece {

    public Rook(ChessGame.TeamColor color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = Factory.getMoveCollection();

        // Up
        int counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() + counter, myPosition.getColumn());
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Down
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() - counter, myPosition.getColumn());
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Left
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow(), myPosition.getColumn() - counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Right
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow(), myPosition.getColumn() + counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        return moves;
    }
}
