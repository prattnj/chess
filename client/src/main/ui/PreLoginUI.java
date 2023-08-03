package ui;

import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.BaseResponse;
import model.response.LoginResponse;
import net.WSConnection;
import util.Esc;

import java.net.URI;

public class PreLoginUI extends Client {

    public void start() {

        out.println(Esc.SET_TEXT_COLOR_WHITE + Esc.RESET_BG_COLOR);
        out.println("Welcome to Chess 1.0!");
        out.println("(" + HELP + ")");

        while (true) {

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

            out.print(Esc.SET_TEXT_COLOR_YELLOW + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
            String input = in.nextLine().toLowerCase();
            switch (input) {
                case "h", "help" -> help();
                case "l", "login" -> {if (login()) new PostLoginUI().start();}
                case "r", "register" -> {if (register()) new PostLoginUI().start();}
                case "q", "quit" -> {return;}
                case "cleardb" -> clear();
                case "auto" -> {if (auto()) new PostLoginUI().start();}
                default -> out.println("Unknown command. " + HELP);
            }
        }
    }

    private void help() {
        out.println("Options:");
        out.println("\"h\", \"help\": See options");
        out.println("\"l\", \"login\": Login as an existing user");
        out.println("\"r\", \"register\": Register a new user");
        out.println("\"q\", \"quit\": Exit the program");
    }

    private boolean login() {
        String username = prompt("Enter your username: ");
        String password = prompt("Enter your password: ");
        out.print("\n");

        LoginRequest request = new LoginRequest(username, password);
        BaseResponse response = server.login(request);
        if (response.isSuccess()) authToken = ((LoginResponse) response).getAuthToken();
        else {
            out.println("Login failed:");
            printError(response.getMessage());
        }
        return response.isSuccess();
    }

    private boolean register() {
        String username = prompt("Enter your username: ");
        String password = prompt("Enter your password: ");
        String email = prompt("Enter your email: ");
        out.print("\n");

        RegisterRequest request = new RegisterRequest(username, password, email);
        BaseResponse response = server.register(request);
        if (response.isSuccess()) authToken = ((LoginResponse) response).getAuthToken();
        else {
            out.println("Register failed:");
            printError(response.getMessage());
        }
        return response.isSuccess();
    }

    private void clear() {
        BaseResponse response = server.clear();
        if (response.isSuccess()) out.println("Database cleared.");
        else {
            out.println("Clear failed:");
            printError(response.getMessage());
        }
    }

    // For testing purposes, registers or logs in a user named "test"
    private boolean auto() {
        LoginRequest request = new LoginRequest("test", "test");
        BaseResponse response = server.login(request);
        if (response.isSuccess()) authToken = ((LoginResponse) response).getAuthToken();
        else {
            RegisterRequest request1 = new RegisterRequest("test", "test", "test");
            BaseResponse response1 = server.register(request1);
            if (response1.isSuccess()) authToken = ((LoginResponse) response1).getAuthToken();
            else {
                out.println("Login failed:");
                printError(response.getMessage());
            }
            return response1.isSuccess();
        }
        return response.isSuccess();
    }
}
