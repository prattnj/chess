package chess;

import util.Factory;
import util.Util;

import java.util.*;

/**
 * Implementation of the ChessGame interface
 */
public class Game implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private final List<ChessMove> moveHistory = new ArrayList<>();
    private boolean isOver = false;

    public Game() {
        teamTurn = TeamColor.WHITE;
        board = Factory.getNewBoard();
        board.resetBoard();
    }

    public Game(String str) {
        // WARNING: Assumes that str is formatted like this.toString()
        String[] parts = str.split(";;");
        isOver = Boolean.parseBoolean(parts[0]);
        teamTurn = parts[1].equals("w") ? TeamColor.WHITE : TeamColor.BLACK;
        board = Factory.getNewBoard(parts[2]);
        if (parts.length > 3) {
            String[] moves = parts[3].split(";");
            for (String s : moves) moveHistory.add(Factory.getNewMove(s));
        }
    }

    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = Factory.getMoveCollection();

        // Filter out moves that put the king in danger
        for (ChessMove move : allMoves) {
            ChessBoard boardCopy = Factory.getNewBoard(board);
            boardCopy.addPiece(move.getEndPosition(), piece);
            boardCopy.addPiece(move.getStartPosition(), null);
            if (!positionIsEndangered(boardCopy, findKing(boardCopy, piece.getTeamColor()))) validMoves.add(move);
        }

        // Add castling / en passant
        if (piece.getPieceType() == ChessPiece.PieceType.KING) validMoves.addAll(addCastling(piece));
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) validMoves.addAll(addEnPassant(piece));

        return validMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

        if (!Util.validatePosition(move.getStartPosition()) || !Util.validatePosition(move.getEndPosition())){
            throw new InvalidMoveException("Error: malformed move");
        }

        ChessPiece mover = board.getPiece(move.getStartPosition());
        if (mover == null) throw new InvalidMoveException("Error: empty square");
        if (mover.getTeamColor() != teamTurn) throw new InvalidMoveException("Error: it is not your turn");

        if (!validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException("Error: invalid move");

        // At this point, move is valid and can be executed
        board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), Factory.getNewPiece(move.getPromotionPiece(), mover.getTeamColor()));
        } else board.addPiece(move.getEndPosition(), mover);

        // Special case for castling: also move the rook
        if (mover.getPieceType() == ChessPiece.PieceType.KING) {
            int diff = move.getEndPosition().getColumn() - move.getStartPosition().getColumn();
            int row = move.getStartPosition().getRow();
            if (diff == 2) {
                // This move is a valid right castle
                board.addPiece(Factory.getNewPosition(row, 6), board.getPiece(Factory.getNewPosition(row, 8)));
                board.addPiece(Factory.getNewPosition(row, 8), null);
            } else if (diff == -2) {
                // This move is a valid left castle
                board.addPiece(Factory.getNewPosition(row, 4), board.getPiece(Factory.getNewPosition(row, 1)));
                board.addPiece(Factory.getNewPosition(row, 1), null);
            }
        }

        // Special case for en passant: also remove the captured pawn
        if (mover.getPieceType() == ChessPiece.PieceType.PAWN) {
            int mod = move.getEndPosition().getColumn() - move.getStartPosition().getColumn();
            board.addPiece(Factory.getNewPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() + mod), null);
        }

        moveHistory.add(move);

        // Determine whether this move ended the game
        TeamColor opponent = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        if (isInCheckmate(opponent) || isInStalemate(opponent)) isOver = true;

        // Update whose turn it is
        toggleTurn();
    }

    @Override
    public Boolean isInCheck(TeamColor teamColor) {
        return positionIsEndangered(board, findKing(board, teamColor));
    }

    @Override
    public Boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    @Override
    public Boolean isInStalemate(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = Factory.getNewPosition(i, j);
                ChessPiece resident = board.getPiece(pos);
                if (resident != null && resident.getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()) return false;
                }
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean isOver() {
        return isOver;
    }

    @Override
    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }

    private Collection<ChessMove> addCastling(ChessPiece king) {

        Collection<ChessMove> moves = Factory.getMoveCollection();
        TeamColor color = king.getTeamColor();

        // CASTLING CONDITIONS:
        // Condition 0: King (0.1) and Rook(s) (0.2) are in the correct position (for abnormal test cases)
        // Condition 1: King (1.1) and Rook(s) (1.2) have not moved
        // Condition 2: There are no pieces between King and Rook(s)
        // Condition 3: The King is not in check
        // Condition 4: Neither piece is in danger after the move

        int row = color == TeamColor.WHITE ? 1 : 8;
        ChessPosition kingPos = findPiece(board, king);
        if (kingPos == null) return moves; // This should never be the case

        ChessPiece leftRook = board.getPiece(Factory.getNewPosition(row, 1));
        ChessPiece rightRook = board.getPiece(Factory.getNewPosition(row, 8));

        // Condition 0.1
        if (!kingPos.equals(Factory.getNewPosition(row, 5))) return moves;

        // Condition 1.1
        if (pieceHasMoved(king)) return moves;

        // Condition 3
        if (isInCheck(color)) return moves;

        // CASTLE LEFT

        // Condition 0.2
        if (leftRook != null && leftRook.getPieceType() == ChessPiece.PieceType.ROOK && leftRook.getTeamColor() == color) {
            // Condition 1.2
            if (!pieceHasMoved(leftRook)) {
                // Condition 2
                boolean condition2 = true;
                for (int i = 2; i <= 4; i++) if (board.getPiece(Factory.getNewPosition(row, i)) != null) condition2 = false;
                if (condition2) {
                    // Condition 4: create copy of board and execute move
                    ChessPosition newKingPos = Factory.getNewPosition(row, 3);
                    ChessPosition newRookPos = Factory.getNewPosition(row, 4);
                    ChessMove move = Factory.getNewMove(kingPos, newKingPos, null);
                    ChessBoard boardCopy = Factory.getNewBoard(board);
                    // Squares to update:
                    // New king square (row, 3) -> king
                    // Old king square (row, 5) -> null
                    // New rook square (row, 4) -> rook
                    // Old rook square (row, 1) -> null
                    boardCopy.addPiece(move.getEndPosition(), king);
                    boardCopy.addPiece(move.getStartPosition(), null);
                    boardCopy.addPiece(newRookPos, leftRook);
                    boardCopy.addPiece(Factory.getNewPosition(row, 1), null);
                    if (!positionIsEndangered(boardCopy, newKingPos) && !positionIsEndangered(boardCopy, newRookPos)) moves.add(move);
                }
            }
        }

        // CASTLE RIGHT

        // Condition 0.2
        if (rightRook != null && rightRook.getPieceType() == ChessPiece.PieceType.ROOK && rightRook.getTeamColor() == color) {
            // Condition 1.2
            if (!pieceHasMoved(rightRook)) {
                // Condition 2
                boolean condition2 = true;
                for (int i = 6; i <= 7; i++) if (board.getPiece(Factory.getNewPosition(row, i)) != null) condition2 = false;
                if (condition2) {
                    // Condition 4: create copy of board and execute move
                    ChessPosition newKingPos = Factory.getNewPosition(row, 7);
                    ChessPosition newRookPos = Factory.getNewPosition(row, 6);
                    ChessMove move = Factory.getNewMove(kingPos, newKingPos, null);
                    ChessBoard boardCopy = Factory.getNewBoard(board);
                    // Squares to update:
                    // New king square (row, 7) -> king
                    // Old king square (row, 5) -> null
                    // New rook square (row, 6) -> rook
                    // Old rook square (row, 8) -> null
                    boardCopy.addPiece(move.getEndPosition(), king);
                    boardCopy.addPiece(move.getStartPosition(), null);
                    boardCopy.addPiece(newRookPos, leftRook);
                    boardCopy.addPiece(Factory.getNewPosition(row, 8), null);
                    if (!positionIsEndangered(boardCopy, newKingPos) && !positionIsEndangered(boardCopy, newRookPos)) moves.add(move);
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> addEnPassant(ChessPiece pawn) {

        Collection<ChessMove> moves = Factory.getMoveCollection();

        ChessPosition startPos = findPiece(board, pawn);
        if (startPos == null) return moves;
        TeamColor color = pawn.getTeamColor();

        // Make sure the last move was a double pawn move
        if (moveHistory.isEmpty()) return moves;
        ChessMove lastMove = moveHistory.get(moveHistory.size() - 1);
        if (!Objects.equals(lastMove.getEndPosition(), Factory.getNewPosition(startPos.getRow(), startPos.getColumn() - 1)) &&
                !Objects.equals(lastMove.getEndPosition(), Factory.getNewPosition(startPos.getRow(), startPos.getColumn() + 1))) {
            return moves;
        }
        if (Math.abs(lastMove.getEndPosition().getRow() - lastMove.getStartPosition().getRow()) != 2) return moves;
        ChessPiece enemy = board.getPiece(lastMove.getEndPosition());
        if (enemy == null || enemy.getPieceType() != ChessPiece.PieceType.PAWN || enemy.getTeamColor() == color) return moves;

        int rowMod = color == TeamColor.WHITE ? 1 : -1;
        moves.add(Factory.getNewMove(startPos, Factory.getNewPosition(startPos.getRow() + rowMod, lastMove.getEndPosition().getColumn()), null));

        return moves;
    }

    private void toggleTurn() {
        teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private boolean pieceHasMoved(ChessPiece piece) {
        ChessPosition pos = findPiece(board, piece);
        for (ChessMove move : moveHistory) if (move.getEndPosition().equals(pos)) return true;
        return false;
    }

    private ChessPosition findPiece(ChessBoard board, ChessPiece piece) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = Factory.getNewPosition(i, j);
                if (board.getPiece(pos) == piece) return pos;
            }
        }
        return null;
    }

    private ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color) {
        // This method assumes that each team has 0-1 kings on the board
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = Factory.getNewPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) return pos;
            }
        }
        return null;
    }

    private boolean positionIsEndangered(ChessBoard board, ChessPosition endPos) {

        if (endPos == null) return false;
        ChessPiece victim = board.getPiece(endPos);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition startPos = Factory.getNewPosition(i, j);
                if (startPos.equals(endPos)) continue;

                ChessPiece attacker = board.getPiece(startPos);
                if (attacker == null || attacker.getTeamColor() == victim.getTeamColor()) continue;
                Collection<ChessMove> moves = attacker.pieceMoves(board, startPos);
                if (moves == null) return false;
                if (moves.contains(Factory.getNewMove(startPos, endPos, null)) ||
                        moves.contains(Factory.getNewMove(startPos, endPos, ChessPiece.PieceType.ROOK)) ||
                        moves.contains(Factory.getNewMove(startPos, endPos, ChessPiece.PieceType.KNIGHT)) ||
                        moves.contains(Factory.getNewMove(startPos, endPos, ChessPiece.PieceType.BISHOP)) ||
                        moves.contains(Factory.getNewMove(startPos, endPos, ChessPiece.PieceType.QUEEN))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isOver);
        sb.append(";;");
        sb.append(teamTurn == TeamColor.WHITE ? "w" : "b");
        sb.append(";;");
        sb.append(board.toString());
        sb.append(";;");
        for (ChessMove move : moveHistory) {
            sb.append(move.toString());
            sb.append(";");
        }
        if (!moveHistory.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
