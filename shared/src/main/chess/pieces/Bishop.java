package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * Bishop implementation of the ChessPiece interface
 */
public class Bishop implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type = PieceType.BISHOP;

    /**
     * Basic constructor
     * @param color This bishop's team color
     */
    public Bishop(ChessGame.TeamColor color) {
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

        // Up and left
        int counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() + counter, myPosition.getColumn() - counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Up and right
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() + counter, myPosition.getColumn() + counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Down and left
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() - counter, myPosition.getColumn() - counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        // Down and right
        counter = 1;
        while (true) {
            ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() - counter, myPosition.getColumn() + counter);
            if (!Util.addMove(myPosition, newPos, board, moves, null)) break;
            counter++;
        }

        return moves;
    }
}
