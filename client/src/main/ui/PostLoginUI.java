package ui;

import chess.ChessGame;
import model.request.BaseRequest;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.BaseResponse;
import model.response.CreateGameResponse;
import model.response.ListGamesObj;
import model.response.ListGamesResponse;
import net.WSConnection;
import util.Esc;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class PostLoginUI extends PreLoginUI {

    private List<ListGamesObj> allGames = null;

    public void start() {

        out.println("Logged in successfully.");
        out.println("(" + HELP + ")");

        updateGames();

        while(true) {

            // refresh connection if necessary
            if (connection.isClosed()) {
                try {
                    connection = new WSConnection(new URI("ws://" + host + ":" + port + "/ws"));
                    connection.connect();
                } catch (Exception e) {
                    printError("Unable to connect to server. Try again later.");
                    quit();
                }
            }

            out.print(Esc.SET_TEXT_COLOR_GREEN + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
            String input = in.nextLine().toLowerCase();
            String[] parts = input.split(" ");
            switch (parts[0]) {
                case "h", "help" -> help();
                case "c", "create" -> create();
                case "ls", "list" -> list();
                case "j", "join" -> join(input);
                case "o", "observe" -> observe(input);
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
        out.println("\"j <gameID>\", \"join <gameID>\": Join an existing game specified by the given game ID");
        out.println("\"o <gameID>\", \"observe <gameID>\": Observe a game specified by the given game ID");
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
        updateGames();
        if (allGames.isEmpty()) out.println("There are currently no games.");
        else for (ListGamesObj game : allGames) printGame(game);
    }

    private void join(String input) {

        String[] parts = input.split(" ");
        if (parts.length < 2) {
            out.println("Must specify a game ID. Enter 'ls' to see games.");
            return;
        }

        int gameID = Integer.parseInt(parts[1]);

        out.print("Would you like to play as (b)lack or (w)hite? ");
        String color = in.nextLine();
        if (color.equalsIgnoreCase("b")) color = "black";
        else if (color.equalsIgnoreCase("w")) color = "white";
        else if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
            out.println("Please enter a valid color.");
        }

        joinOrObserve(color, gameID, true);
    }

    private void observe(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            out.println("Must specify a game ID. Enter 'ls' to see games.");
            return;
        }

        int gameID = Integer.parseInt(parts[1]);

        joinOrObserve(null, gameID, false);
    }

    private void joinOrObserve(String color, int gameID, boolean isJoin) {
        JoinGameRequest request = new JoinGameRequest(color, gameID);
        BaseResponse response = server.join(request, authToken);
        if (response.isSuccess()) {
            updateGames();
            out.println("Successfully joined game " + gameID);
            new GameUI().start(gameID, ChessGame.TeamColor.valueOf((isJoin ? color : "white").toUpperCase()), isJoin);
        }
        else {
            out.println("Could not join game: ");
            printError(response.getMessage());
        }
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
}
