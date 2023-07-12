package chess.pieces;

import chess.*;
import util.Factory;

import java.util.Collection;

/**
 * Queen implementation of the ChessPiece interface
 */
public class Queen implements ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type = PieceType.QUEEN;

    /**
     * Basic constructor
     * @param color This queen's team color
     */
    public Queen(ChessGame.TeamColor color) {
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

        ChessPiece rook = Factory.getNewPiece(PieceType.ROOK, color);
        ChessPiece bishop = Factory.getNewPiece(PieceType.BISHOP, color);

        Collection<ChessMove> moves = Factory.getMoveCollection();
        moves.addAll(rook.pieceMoves(board, myPosition));
        moves.addAll(bishop.pieceMoves(board, myPosition));

        return moves;
    }
}
