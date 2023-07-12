package chess.pieces;

import chess.*;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * King implementation of the ChessPiece interface
 */
public class King implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type = PieceType.KING;

    /**
     * Basic constructor
     * @param color This king's team color
     */
    public King(ChessGame.TeamColor color) {
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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChessPosition newPos = Factory.getNewPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                Util.addMove(myPosition, newPos, board, moves, null);
            }
        }

        return moves;
    }
}
