package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 */
public class ServerMessage {

    private final ServerMessageType serverMessageType;
    private String message;
    private String game;
    private String errorMessage;

    /**
     * Creates a new ServerMessage
     * @param type Type of message
     * @param message Message to send
     */
    public ServerMessage(ServerMessageType type, String message){
        this.serverMessageType = type;
        if (type == ServerMessageType.NOTIFICATION) this.message = message;
        else if (type == ServerMessageType.ERROR) this.errorMessage = message;
        else this.game = message;
    }

    public enum ServerMessageType{
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return switch (serverMessageType) {
            case NOTIFICATION -> message;
            case ERROR -> errorMessage;
            case LOAD_GAME -> game;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerMessage that)) return false;
        return getServerMessageType() == that.getServerMessageType() && Objects.equals(message, that.getMessage())
                && Objects.equals(game, that.getMessage())
                && Objects.equals(errorMessage, that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), message);
    }
}
