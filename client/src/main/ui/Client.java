package ui;

import net.ServerFacade;

import java.io.PrintStream;
import java.util.Scanner;

public class Client {

    protected static String host = "localhost";
    protected static String port = "3000";
    protected static String authToken;
    protected static ServerFacade server;
    protected final Scanner in = new Scanner(System.in);
    protected final PrintStream out = System.out;
    protected final String HELP = "Enter \"h\" or \"help\" for options";
    protected static final String EXIT_MESSAGE = "Happy trails!";

    public static void main(String[] args) {

        if (args.length >= 2){
            host = args[0];
            port = args[1];
        }
        server = new ServerFacade(host, port);

        new PreLoginUI().start();
        System.out.println(EXIT_MESSAGE);
    }

    protected void printError(String error) {
        System.out.println(Esc.SET_TEXT_COLOR_RED + error + Esc.SET_TEXT_COLOR_WHITE);
    }

    protected void quit() {
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }
}
