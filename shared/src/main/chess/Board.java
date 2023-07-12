package chess;

import util.Factory;

/**
 * Implementation of the ChessBoard interface
 */
public class Board implements ChessBoard {

    private final ChessPiece[][] board;

    /**
     * Initializes the board array with null ChessPiece objects
     */
    public Board() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Initializes this as a deep copy of an already existing board
     * @param board The board to create a deep copy of
     */
    public Board(ChessBoard board) {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) for (int j = 0; j < 8; j++) this.board[i][j] = board.getPiece(Factory.getNewPosition(i + 1, j + 1));
    }

    public Board(String str) {
        // WARNING: Assumes that str is formatted like this.toString()
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            board[i / 8][i % 8] = Factory.getNewPiece(getTypeForChar(c), color);
        }
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public void resetBoard() {

        // CLEAR EXISTING PIECES
        for (int i = 0; i < 8; i++) for (int j = 0; j < 8; j++) board[i][j] = null;

        // ADD WHITE PIECES
        board[0][0] = Factory.getNewPiece(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.WHITE);
        board[0][1] = Factory.getNewPiece(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.WHITE);
        board[0][2] = Factory.getNewPiece(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.WHITE);
        board[0][3] = Factory.getNewPiece(ChessPiece.PieceType.QUEEN, ChessGame.TeamColor.WHITE);
        board[0][4] = Factory.getNewPiece(ChessPiece.PieceType.KING, ChessGame.TeamColor.WHITE);
        board[0][5] = Factory.getNewPiece(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.WHITE);
        board[0][6] = Factory.getNewPiece(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.WHITE);
        board[0][7] = Factory.getNewPiece(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.WHITE);
        for (int i = 0; i < 8; i++) board[1][i] = Factory.getNewPiece(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.WHITE);

        // ADD BLACK PIECES
        board[7][0] = Factory.getNewPiece(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.BLACK);
        board[7][1] = Factory.getNewPiece(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.BLACK);
        board[7][2] = Factory.getNewPiece(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.BLACK);
        board[7][3] = Factory.getNewPiece(ChessPiece.PieceType.QUEEN, ChessGame.TeamColor.BLACK);
        board[7][4] = Factory.getNewPiece(ChessPiece.PieceType.KING, ChessGame.TeamColor.BLACK);
        board[7][5] = Factory.getNewPiece(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.BLACK);
        board[7][6] = Factory.getNewPiece(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.BLACK);
        board[7][7] = Factory.getNewPiece(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.BLACK);
        for (int i = 0; i < 8; i++) board[6][i] = Factory.getNewPiece(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.BLACK);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) for (int j = 0; j < 8; j++) sb.append(getCharForPiece(board[i][j]));
        return sb.toString();
    }

    private char getCharForPiece(ChessPiece piece) {
        if (piece == null) return '-';
        char c = switch (piece.getPieceType()) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case ROOK -> 'R';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case PAWN -> 'P';
        };
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? c : Character.toLowerCase(c);
    }

    private ChessPiece.PieceType getTypeForChar(char c) {
        return switch (c) {
            case 'K', 'k' -> ChessPiece.PieceType.KING;
            case 'Q', 'q' -> ChessPiece.PieceType.QUEEN;
            case 'R', 'r' -> ChessPiece.PieceType.ROOK;
            case 'N', 'n' -> ChessPiece.PieceType.KNIGHT;
            case 'B', 'b' -> ChessPiece.PieceType.BISHOP;
            case 'P', 'p' -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }
}
