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

    public interface GameUI {
        void setGame(ChessGame game);
        void notify(String message, boolean isError);
    }

    public void assignGameUI(GameUI ui) {
        this.ui = ui;
    }
}
