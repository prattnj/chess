package chess.pieces;

import chess.*;

import java.util.Collection;

public abstract class Piece implements ChessPiece {

    protected final ChessGame.TeamColor color;
    private final PieceType pieceType;

    public Piece(ChessGame.TeamColor color, PieceType pieceType) {
        this.color = color;
        this.pieceType = pieceType;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
