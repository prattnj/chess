package net;

import chess.ChessGame;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
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
        System.out.println("Received message: " + s);

        ServerMessage message = gson.fromJson(s, ServerMessage.class);
        if (message == null) return; // invalid message from server



        // todo parse message and set the game object in the ui
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        // todo: if i == 123, return back to the PostLoginUI, because this was the result of a resign
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public interface GameUI {
        void setGame(ChessGame game);
        void drawBoard();
        void notify(String message);
    }

    public void assignGameUI(GameUI ui) {
        this.ui = ui;
    }
}
