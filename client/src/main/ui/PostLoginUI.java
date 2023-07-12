package ui;

import model.request.BaseRequest;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.BaseResponse;
import model.response.CreateGameResponse;
import model.response.ListGamesObj;
import model.response.ListGamesResponse;
import util.Factory;

import java.util.Arrays;
import java.util.List;

public class PostLoginUI extends PreLoginUI {

    private List<ListGamesObj> allGames = null;

    public void start() {

        out.println("Logged in successfully.");
        out.println("(" + HELP + ")");

        updateGames();

        while(true) {
            out.print(Esc.SET_TEXT_COLOR_GREEN + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
            String input = in.nextLine().toLowerCase();
            switch (input) {
                case "h", "help" -> help();
                case "c", "create" -> create();
                case "ls", "list" -> list();
                case "j", "join" -> join();
                case "o", "observe" -> observe();
                case "lg", "logout" -> {if (logout()) return;}
                case "q", "quit" -> quit();
                default -> out.println("Unknown command. " + HELP);
            }
        }

    }

    private void help() {
        out.println("Options:");
        out.println("\"h\", \"help\": See options");
        out.println("\"c\", \"create\": Create a new game");
        out.println("\"ls\", \"list\": List all existing games");
        out.println("\"j\", \"join\": Join an existing game");
        out.println("\"o\", \"observe\": Observe a game");
        out.println("\"lg\", \"logout\": Logout");
        out.println("\"q\", \"quit\": Exit the program");
    }

    private boolean logout() {
        BaseResponse response = server.logout(authToken);
        if (response.isSuccess()) {
            authToken = null;
            out.println("Logged out successfully.");
        }
        else {
            out.println("Logout failed:");
            printError(response.getMessage());
        }
        return response.isSuccess();
    }

    private void create() {
        out.print("Give this new game a name: ");
        String name = in.nextLine();
        out.print("\n");

        BaseRequest request = new CreateGameRequest(name);
        BaseResponse response = server.create(request, authToken);
        if (response.isSuccess()) {
            int gameID = ((CreateGameResponse) response).getGameID();
            out.println("Game successfully created with ID " + gameID);
            updateGames();
        }
        else {
            out.println("Game creation failed: ");
            printError(response.getMessage());
        }
    }

    private void list() {
        if (allGames.isEmpty()) out.println("There are currently no games.");
        else for (ListGamesObj game : allGames) printGame(game);
    }

    private void join() {
        out.print("Enter the ID of the game you'd like to join: ");
        int gameID = Integer.parseInt(in.nextLine());
        out.print("Would you like to play as (b)lack or (w)hite? ");
        String color = in.nextLine();
        if (color.equalsIgnoreCase("b")) color = "black";
        else if (color.equalsIgnoreCase("w")) color = "white";

        JoinGameRequest request = new JoinGameRequest(color, gameID);
        BaseResponse response = server.join(request, authToken);
        if (response.isSuccess()) {
            out.println("Successfully joined game " + gameID + " as the " + color + " player.");
            drawGame(Factory.getNewGame().getBoard().toString());
        }
        else {
            out.println("Could not join game: ");
            printError(response.getMessage());
        }
        updateGames();
    }

    private void observe() {
        out.print("Enter the ID of the game you'd like to observe: ");
        int gameID = Integer.parseInt(in.nextLine());

        JoinGameRequest request = new JoinGameRequest(null, gameID);
        BaseResponse response = server.join(request, authToken);
        if (response.isSuccess()) {
            out.println("Successfully observing game " + gameID);
            drawGame(Factory.getNewGame().getBoard().toString());
        }
        else {
            out.println("Could not observe game: ");
            printError(response.getMessage());
        }
        updateGames();
    }

    private void updateGames() {
        BaseResponse listResp = server.list(authToken);
        if (listResp.isSuccess()) allGames = Arrays.asList(((ListGamesResponse) listResp).getGames());
        else {
            printError("Server error, exiting. Try again later.");
            System.exit(0);
        }
    }

    private void printGame(ListGamesObj game) {
        out.println("ID: " + game.getGameID());
        out.println("Name: " + game.getGameName());
        out.println("White: " + (game.getWhiteUsername() == null ? "" : game.getWhiteUsername()));
        out.println("Black: " + (game.getBlackUsername() == null ? "" : game.getBlackUsername()));
        out.print("\n");
    }

    private void drawGame(String board) {

        // WHITE'S PERSPECTIVE
        printAlphaLabel(true);
        printBoard(board, true);
        printAlphaLabel(true);

        // separator
        out.print(Esc.SET_BG_COLOR_WHITE + "   ");
        for (int i = 0; i < 8; i++) out.print(Esc.EMPTY);
        out.print("   " + Esc.RESET_BG_COLOR + "\n");

        // BLACK'S PERSPECTIVE
        printAlphaLabel(false);
        printBoard(board, false);
        printAlphaLabel(false);
    }

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
        /*if (Character.isAlphabetic(c)) return " " + c + " ";
        else return Esc.EMPTY;*/
    }
}
