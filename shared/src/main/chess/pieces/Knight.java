package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * Knight implementation of the ChessPiece interface
 */
public class Knight implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type = PieceType.KNIGHT;

    /**
     * Basic constructor
     * @param color This knight's team color
     */
    public Knight(ChessGame.TeamColor color) {
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
