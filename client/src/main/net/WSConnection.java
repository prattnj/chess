package net;

import chess.ChessGame;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import util.Factory;
import webSocketMessages.serverMessages.ServerMessage;

import java.net.URI;

public class WSConnection extends WebSocketClient {

    private GameUI ui;
    private final Gson gson = new Gson();

    /**
     * Creates a new WebSocket connection to the server
     * @param serverUri the URI of the server
     */
    public WSConnection(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {}

    @Override
    public void onMessage(String s) {

        ServerMessage message = gson.fromJson(s, ServerMessage.class);
        if (message == null) return; // invalid message from server

        switch (message.getServerMessageType()) {
            case NOTIFICATION -> ui.notify(message.getMessage(), false);
            case LOAD_GAME -> ui.setGame(Factory.getNewGame(message.getMessage()));
            case ERROR -> ui.notify(message.getMessage(), true);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {}

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    /**
     * Interface for the GameUI to implement so that the WSConnection can notify the UI
     */
    public interface GameUI {

        /**
         * Sets the game object in the UI
         * @param game
         */
        void setGame(ChessGame game);

        /**
         * Notifies the UI of a message from the server and prints it
         * @param message
         * @param isError
         */
        void notify(String message, boolean isError);
    }

    /**
     * Assigns the GameUI to the WSConnection
     * @param ui the GameUI to assign
     */
    public void assignGameUI(GameUI ui) {
        this.ui = ui;
    }
}
