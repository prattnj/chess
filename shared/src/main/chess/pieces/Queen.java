package chess.pieces;

import chess.*;
import util.Factory;

import java.util.Collection;

/**
 * Queen implementation of the ChessPiece interface
 */
public class Queen extends Piece {

    public Queen(ChessGame.TeamColor color) {
        super(color, PieceType.QUEEN);
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
