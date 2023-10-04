package server;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import service.*;
import spark.Spark;
import util.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Where it all begins: the class that listens for incoming requests and
 * redirects them using the Spark framework
 */
public class Server {

    private void run(int port) {

        // Set up port
        Spark.port(port);

        // Set up static files
        Spark.externalStaticFileLocation("web/build");

        // Set up web socket
        Spark.webSocket("/ws", new WebSocketHandler());

        // Set up endpoints
        Spark.delete("/db", new Handler(new ClearService(), null, false));
        Spark.post("/user", new Handler(new RegisterService(), RegisterRequest.class, false));
        Spark.post("/session", new Handler(new LoginService(), LoginRequest.class, false));
        Spark.delete("/session", new Handler(new LogoutService(), null, true));
        Spark.post("/game", new Handler(new CreateGameService(), CreateGameRequest.class, true));
        Spark.put("/game", new Handler(new JoinGameService(), JoinGameRequest.class, true));
        Spark.get("/game", new Handler(new ListGamesService(), null, true));

        Spark.init();
    }

    /**
     * The starting point for the server program
     * @param args Any custom options, see usage statement
     */
    public static void main(String[] args) {

        Set<String> validDbTypes = new HashSet<>();
        validDbTypes.add("ram");
        validDbTypes.add("mysql");

        String dbType;
        String usage = "Usage: java server.Server.main <port> [database]";

        if (args == null || args.length == 0) {
            System.out.println(usage);
            return;
        }
        try {
            // look for and assign command line arguments
            int port = Integer.parseInt(args[0]);

            if (args.length > 1) {
                dbType = args[1];
                if (!validDbTypes.contains(dbType)) throw new RuntimeException("Invalid database type");
            } else dbType = "ram";
            Util.CURRENT_DAO_TYPE = dbType;

            // run server
            System.out.println("Server listening on port " + port + "...");
            new Server().run(port);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(usage);
        }
    }

}
