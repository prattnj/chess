package ui;

import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.BaseResponse;
import model.response.LoginResponse;

public class PreLoginUI extends Client {

    public void start() {

        out.println(Esc.SET_TEXT_COLOR_WHITE + Esc.RESET_BG_COLOR);
        out.println("Welcome to Chess 1.0!");
        out.println("(" + HELP + ")");

        while (true) {
            out.print(Esc.SET_TEXT_COLOR_YELLOW + "\nchess> " + Esc.SET_TEXT_COLOR_WHITE);
            String input = in.nextLine().toLowerCase();
            switch (input) {
                case "h", "help" -> help();
                case "l", "login" -> {if (login()) new PostLoginUI().start();}
                case "r", "register" -> {if (register()) new PostLoginUI().start();}
                case "q", "quit" -> {return;}
                case "clear" -> clear();
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
        out.print("Enter your username: ");
        String username = in.nextLine();
        out.print("Enter your password: ");
        String password = in.nextLine();
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
        out.print("Enter your username: ");
        String username = in.nextLine();
        out.print("Enter your password: ");
        String password = in.nextLine();
        out.print("Enter your email: ");
        String email = in.nextLine();
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
        if (response.isSuccess()) out.println("Clear succeeded.");
        else {
            out.println("Clear failed:");
            printError(response.getMessage());
        }
    }

    private boolean auto() {
        LoginRequest request = new LoginRequest("test", "test");
        BaseResponse response = server.login(request);
        if (response.isSuccess()) authToken = ((LoginResponse) response).getAuthToken();
        else {
            out.println("Login failed:");
            printError(response.getMessage());
        }
        return response.isSuccess();
    }
}
