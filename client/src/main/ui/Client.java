package ui;

import com.google.gson.Gson;
import net.ServerFacade;
import net.WSConnection;
import util.Esc;

import java.io.PrintStream;
import java.net.URI;
import java.util.Scanner;

public class Client {

    protected static String host = "136.36.39.119";
    protected static String port = "80";
    protected static String authToken;
    protected static ServerFacade server;
    protected static WSConnection connection = null;
    protected final Scanner in = new Scanner(System.in);
    protected final PrintStream out = System.out;
    protected final Gson gson = new Gson();
    protected final String HELP = "Enter \"h\" or \"help\" for options";
    protected static final String EXIT_MESSAGE = "Happy trails!";

    public static void main(String[] args) {

        if (args.length >= 2){
            host = args[0];
            port = args[1];
        }
        server = new ServerFacade(host, port);

        try {
            connection = new WSConnection(new URI("ws://" + host + ":" + port + "/ws"));
            connection.connect();
        } catch (Exception e) {
            printError("Unable to connect to server. Try again later.");
            quit();
        }

        new PreLoginUI().start();
        quit();
    }

    protected static void printError(String error) {
        System.out.println(Esc.SET_TEXT_COLOR_RED + error + Esc.SET_TEXT_COLOR_WHITE);
    }

    public static void quit() {
        if (connection != null) connection.close();
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }
}
