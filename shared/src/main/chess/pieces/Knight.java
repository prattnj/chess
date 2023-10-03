package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * Knight implementation of the ChessPiece interface
 */
public class Knight extends Piece {

    public Knight(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = Factory.getMoveCollection();

        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), board, moves, null);
        Util.addMove(myPosition, Factory.getNewPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), board, moves, null);

        return moves;
    }
}
