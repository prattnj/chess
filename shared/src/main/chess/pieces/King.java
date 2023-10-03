package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * King implementation of the ChessPiece interface
 */
public class King extends Piece {

    public King(ChessGame.TeamColor color) {
        super(color, PieceType.KING);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = Factory.getMoveCollection();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                Util.addMove(myPosition, newPos, board, moves, null);
            }
        }

        return moves;
    }
}
