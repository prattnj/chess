package ui;

import chess.ChessGame;
import model.wsMessage.userCommand.JoinPlayerUC;
import net.WSConnection;
import util.Esc;
import webSocketMessages.userCommands.UserGameCommand;

public class GameUI extends Client implements WSConnection.GameUI {

    private ChessGame game;
    private ChessGame.TeamColor color;
    private final UserGameCommand.CommandType JOIN_OBSERVER = UserGameCommand.CommandType.JOIN_OBSERVER;
    private final UserGameCommand.CommandType LEAVE = UserGameCommand.CommandType.LEAVE;
    private final UserGameCommand.CommandType RESIGN = UserGameCommand.CommandType.RESIGN;

    public GameUI() {
        connection.assignGameUI(this);
    }

    public void start(int gameID, ChessGame.TeamColor color, boolean isPlayer) {
        this.color = color;

        out.println("\nEntered in-game mode.");
        out.println("(" + HELP + ")");

        connection.send(gson.toJson(isPlayer ? new JoinPlayerUC(authToken, gameID, color) : new UserGameCommand(JOIN_OBSERVER, authToken, gameID)));

        while(true) {
            out.print(Esc.SET_TEXT_COLOR_BLUE + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
            String input = in.nextLine().toLowerCase();
            switch (input) {
                case "h", "help" -> help();
                case "d", "draw" -> redraw();
                case "l", "leave" -> {if (leave()) return;}
                case "m", "move" -> move();
                case "s", "show" -> show();
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
        return true;
    }

    private void move() {}

    private void show() {}

    private void resign() {}

    @Override
    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public void drawBoard() {
        boolean isWhite = color != ChessGame.TeamColor.BLACK;
        printAlphaLabel(isWhite);
        printBoard(game.getBoard().toString(), isWhite);
        printAlphaLabel(isWhite);
    }

    @Override
    public void notify(String message) {
        out.println(message);
    }

    // HELPER METHODS
    private void printAlphaLabel(boolean isWhite) {
        out.print(Esc.SET_BG_COLOR_LIGHT_GREY + Esc.SET_TEXT_COLOR_BLACK + "   ");
        if (isWhite) for (int i = 0; i < 8; i++) out.print(" " + (char)('a' + i) + "\u2003");
        else for (int i = 7; i >= 0; i--) out.print(" " + (char)('a' + i) + "\u2003");
        out.println("   " + Esc.RESET_BG_COLOR);
    }

    private void printBoard(String board, boolean isWhite) {
        boolean isLight = true;
        for (int i = 0; i < 8; i++) {
            int rowIndex = isWhite ? 8 - i : i + 1;
            out.print(Esc.SET_BG_COLOR_LIGHT_GREY + " " + rowIndex + " ");
            for (int j = 0; j < 8; j++) {
                int index = isWhite ? ((7 - i) * 8) + j : (i * 8) + (7 - j);
                if (isLight) out.print(Esc.SET_BG_COLOR_LIGHT_SQUARE);
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
            case 'K' -> Esc.WHITE_KING;
            case 'Q' -> Esc.WHITE_QUEEN;
            case 'R' -> Esc.WHITE_ROOK;
            case 'N' -> Esc.WHITE_KNIGHT;
            case 'B' -> Esc.WHITE_BISHOP;
            case 'P' -> Esc.WHITE_PAWN;
            case 'k' -> Esc.BLACK_KING;
            case 'q' -> Esc.BLACK_QUEEN;
            case 'r' -> Esc.BLACK_ROOK;
            case 'n' -> Esc.BLACK_KNIGHT;
            case 'b' -> Esc.BLACK_BISHOP;
            case 'p' -> Esc.BLACK_PAWN;
            default -> Esc.EMPTY;
        };
    }
}
