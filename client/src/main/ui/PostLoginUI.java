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
import util.Util;

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
                case "c", "create" -> create(input);
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
        out.println("\"c <name>\", \"create <name>\": Create a new game");
        out.println("\"ls\", \"list\": List all existing games");
        out.println("\"j <gameID>\", \"join <gameID>\": Join an existing game specified by the given game ID");
        out.println("\"o <gameID>\", \"observe <gameID>\": Observe a game specified by the given game ID");
        out.println("\"lg\", \"logout\": Logout");
        out.println("\"q\", \"quit\": Exit the program");
    }

    private boolean logout() {
        // send logout request to server
        BaseResponse response = server.logout(authToken);
        if (response.isSuccess()) {
            authToken = null;
            out.println("Logged out successfully.");
        } else printError(response.getMessage());
        return response.isSuccess();
    }

    private void create(String input) {

        // determine game name
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            out.println("Must specify a game name.");
            return;
        }

        // send create request to server and print gameID if successful
        BaseRequest request = new CreateGameRequest(parts[1]);
        BaseResponse response = server.create(request, authToken);
        if (response.isSuccess()) {
            int gameID = ((CreateGameResponse) response).getGameID();
            out.println("Game successfully created with ID " + gameID);
            updateGames();
        } else printError(response.getMessage());
    }

    private void list() {
        updateGames();
        if (allGames.isEmpty()) out.println("There are currently no games.");
        else for (ListGamesObj game : allGames) printGame(game);
    }

    private void join(String input) {

        // determine gameID
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            out.println("Must specify a game ID. Enter 'ls' to see games.");
            return;
        }
        int gameID = Integer.parseInt(parts[1]);

        // finish building request
        ChessGame.TeamColor teamColor = Util.getColorForString(prompt("Would you like to play as (b)lack or (w)hite? "));
        if (teamColor == null) {
            out.println("Invalid color.");
            return;
        }

        joinOrObserve(teamColor, gameID, true);
    }

    private void observe(String input) {

        // determine gameID
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            out.println("Must specify a game ID. Enter 'ls' to see games.");
            return;
        }
        int gameID = Integer.parseInt(parts[1]);

        joinOrObserve(null, gameID, false);
    }

    private void joinOrObserve(ChessGame.TeamColor color, int gameID, boolean isJoin) {

        // send join request to server
        JoinGameRequest request = new JoinGameRequest(Util.getStringForColor(color), gameID);
        BaseResponse response = server.join(request, authToken);
        if (response.isSuccess()) {
            updateGames();
            out.println("Successfully joined game " + gameID);
            new GameUI().start(gameID, color, isJoin);
        } else printError(response.getMessage());
    }

    // HELPER METHODS
    private void updateGames() {

        // send list games request to server
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
