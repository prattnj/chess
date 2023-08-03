package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.wsMessage.userCommand.JoinPlayerUC;
import model.wsMessage.userCommand.MakeMoveUC;
import net.WSConnection;
import util.Esc;
import util.Factory;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Collection;
import java.util.HashSet;

public class GameUI extends Client implements WSConnection.GameUI {

    private ChessGame game;
    private int gameID;
    private boolean isPlayer;
    private ChessGame.TeamColor color;
    private final UserGameCommand.CommandType JOIN_OBSERVER = UserGameCommand.CommandType.JOIN_OBSERVER;
    private final UserGameCommand.CommandType LEAVE = UserGameCommand.CommandType.LEAVE;
    private final UserGameCommand.CommandType RESIGN = UserGameCommand.CommandType.RESIGN;

    public GameUI() {
        connection.assignGameUI(this);
    }

    public void start(int gameID, ChessGame.TeamColor color, boolean isPlayer) {
        this.color = color;
        this.gameID = gameID;
        this.isPlayer = isPlayer;

        out.println("\nEntered in-game mode.");
        out.println("(" + HELP + ")");

        connection.send(gson.toJson(isPlayer ? new JoinPlayerUC(authToken, gameID, color) : new UserGameCommand(JOIN_OBSERVER, authToken, gameID)));

        while(true) {
            basePrompt();
            String input = in.nextLine().toLowerCase();
            switch (input) {
                case "h", "help" -> help();
                case "d", "draw" -> redraw();
                case "l", "leave" -> {if (leave()) return;}
                case "m", "move" -> move(input);
                case "s", "show" -> show(input);
                case "r", "resign" -> resign();
                case "test" -> connection.send("testing");
                default -> out.println("Unknown command. " + HELP);
            }
        }
    }

    private void help() {
        out.println("Options:");
        out.println("\"h\", \"help\": See options");
        out.println("\"d\", \"draw\": Redraw the board");
        out.println("\"l\", \"leave\": Leave current game");
        out.println("\"m\", \"move\": Make a move");
        out.println("\"s\", \"show\": Show available moves for a piece");
        out.println("\"r\", \"resign\": Resign (game over)");
    }

    private void redraw() {
        drawBoard();
    }

    private boolean leave() {
        out.println("Leaving game.");
        connection.send(gson.toJson(new UserGameCommand(LEAVE, authToken, gameID)));
        return true;
    }

    private void move(String input) {
        if (game.isOver()) {
            out.println("The game is over. No moves can be made.");
            return;
        }

        String[] parts = input.split(" ");
        String startPosStr;
        String endPosStr;
        if (parts.length > 2) {
            startPosStr = parts[1];
            endPosStr = parts[2];
        } else {
            startPosStr = prompt("Enter the position of the piece to move (a5, d4...): ");
            endPosStr = prompt("Enter the position you'd like to move to: ");
        }
        if (!validatePosition(startPosStr) || !validatePosition(endPosStr)) {
            printError("Invalid position.");
            return;
        }
        ChessPosition start = Factory.getNewPosition(startPosStr);
        ChessPosition end = Factory.getNewPosition(endPosStr);
        Collection<ChessMove> validMoves = game.validMoves(start);

        // get promotion piece if applicable
        ChessPiece.PieceType promo = null;
        if (validMoves.contains(Factory.getNewMove(start, end, ChessPiece.PieceType.QUEEN))) {
            char promoStr = prompt("Enter a piece type for pawn promotion (q, r, n, b): ").charAt(0);
            promo = switch (Character.toLowerCase(promoStr)) {
                case 'q' -> ChessPiece.PieceType.QUEEN;
                case 'r' -> ChessPiece.PieceType.ROOK;
                case 'n' -> ChessPiece.PieceType.KNIGHT;
                case 'b' -> ChessPiece.PieceType.BISHOP;
                default -> ChessPiece.PieceType.KING;
            };
            if (promo == ChessPiece.PieceType.KING) {
                printError("Invalid piece type.");
                return;
            }
        }

        // validate move
        ChessMove move = Factory.getNewMove(start, end, promo);
        if (!validMoves.contains(move)) {
            printError("Invalid move.");
            return;
        }

        // move is valid
        connection.send(gson.toJson(new MakeMoveUC(authToken, gameID, move)));
    }

    private void show(String input) {
        String[] parts = input.split(" ");
        String posStr;
        if (parts.length > 1) posStr = parts[1];
        else posStr = prompt("Enter the position (i.e. a5, d4...) whose moves to show: ");
        if (!validatePosition(posStr)) {
            printError("Invalid position.");
            return;
        }
        ChessPosition pos = Factory.getNewPosition(posStr);
        Collection<ChessMove> possibleMoves = game.validMoves(pos);
        Collection<ChessPosition> endPositions = new HashSet<>();
        for (ChessMove m : possibleMoves) endPositions.add(m.getEndPosition());
        drawBoard(endPositions);
    }

    private void resign() {

        // Validate resignation
        if (!isPlayer) {
            printError("You can't resign as an observer.");
            return;
        }

        out.print("Are you sure you want to resign? (y/n): ");
        String resign = String.valueOf(in.nextLine().charAt(0));
        if (!resign.equalsIgnoreCase("y")) {
            if (!resign.equalsIgnoreCase("n")) printError("Invalid input.");
            return;
        }
        connection.send(gson.toJson(new UserGameCommand(RESIGN, authToken, gameID)));
    }

    @Override
    public void setGame(ChessGame game) {
        this.game = game;
        drawBoard();
        ChessGame.TeamColor color = game.getTeamTurn();
        ChessGame.TeamColor otherColor = color == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        if (game.isOver()) {
            if (game.isInCheckmate(color)) out.println("Checkmate! The " + otherColor + " player wins.");
            else if (game.isInStalemate(color)) out.println("Stalemate! The game is over.");
        } else if (game.isInCheck(color)) out.println("The " + color + " player is in check.");
        basePrompt();
    }

    @Override
    public void notify(String message, boolean isError) {
        if (isError) printError(message);
        else out.println(message);
        basePrompt();
    }

    // HELPER METHODS
    public void drawBoard() {
        drawBoard(new HashSet<>());
    }

    public void drawBoard(Collection<ChessPosition> endPositions) {
        if (game == null) return;
        out.print("\n");
        boolean isWhite = color != ChessGame.TeamColor.BLACK;
        printAlphaLabel(isWhite);
        printBoard(game.getBoard().toString(), isWhite, endPositions);
        printAlphaLabel(isWhite);
        out.print(Esc.SET_TEXT_COLOR_WHITE);
    }

    private void printAlphaLabel(boolean isWhite) {
        out.print(Esc.SET_BG_COLOR_LIGHT_GREY + Esc.SET_TEXT_COLOR_BLACK + "   ");
        if (isWhite) for (int i = 0; i < 8; i++) out.print(" " + (char)('a' + i) + "\u2003");
        else for (int i = 7; i >= 0; i--) out.print(" " + (char)('a' + i) + "\u2003");
        out.println("   " + Esc.RESET_BG_COLOR);
    }

    private void printBoard(String board, boolean isWhite, Collection<ChessPosition> endPositions) {
        boolean isLight = true;
        for (int i = 0; i < 8; i++) {
            int rowIndex = isWhite ? 8 - i : i + 1;
            out.print(Esc.SET_BG_COLOR_LIGHT_GREY + " " + rowIndex + " ");
            for (int j = 0; j < 8; j++) {
                int index = isWhite ? ((7 - i) * 8) + j : (i * 8) + (7 - j);
                int columnIndex = isWhite ? j + 1 : 8 - j;
                ChessPosition position = Factory.getNewPosition(rowIndex, columnIndex);

                if (isLight && endPositions.contains(position)) out.print(Esc.SET_BG_COLOR_GREEN);
                else if (isLight) out.print(Esc.SET_BG_COLOR_LIGHT_SQUARE);
                else if (endPositions.contains(position)) out.print(Esc.SET_BG_COLOR_DARK_GREEN);
                else out.print(Esc.SET_BG_COLOR_DARK_SQUARE);
                out.print(renderPiece(board.charAt(index)));
                isLight = !isLight;
            }
            out.println(Esc.SET_BG_COLOR_LIGHT_GREY + " " + rowIndex + " " + Esc.RESET_BG_COLOR);
            isLight = !isLight;
        }
    }

    private String renderPiece(char c) {
        return switch (c) {
            case 'K' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_KING + Esc.SET_TEXT_COLOR_BLACK;
            case 'Q' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_QUEEN + Esc.SET_TEXT_COLOR_BLACK;
            case 'R' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_ROOK + Esc.SET_TEXT_COLOR_BLACK;
            case 'N' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_KNIGHT + Esc.SET_TEXT_COLOR_BLACK;
            case 'B' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_BISHOP + Esc.SET_TEXT_COLOR_BLACK;
            case 'P' -> Esc.SET_TEXT_COLOR_WHITE + Esc.BLACK_PAWN + Esc.SET_TEXT_COLOR_BLACK;
            case 'k' -> Esc.BLACK_KING;
            case 'q' -> Esc.BLACK_QUEEN;
            case 'r' -> Esc.BLACK_ROOK;
            case 'n' -> Esc.BLACK_KNIGHT;
            case 'b' -> Esc.BLACK_BISHOP;
            case 'p' -> Esc.BLACK_PAWN;
            default -> Esc.EMPTY;
        };
    }

    private boolean validatePosition(String posStr) {
        if (posStr.length() != 2) return false;
        if (!Character.isAlphabetic(posStr.charAt(0)) || !Character.isDigit(posStr.charAt(1))) return false;
        posStr = posStr.toLowerCase();
        if (posStr.charAt(0) < 'a' || posStr.charAt(0) > 'h') return false;
        int i = Integer.parseInt(String.valueOf(posStr.charAt(1)));
        return i >= 1 && i <= 8;
    }

    private void basePrompt() {
        out.print(Esc.SET_TEXT_COLOR_BLUE + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
    }
}
