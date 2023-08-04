package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 */
public class UserGameCommand {

    protected CommandType commandType;
    protected final String authToken;
    protected final Integer gameID;

    /**
     * Creates a new UserGameCommand
     * @param commandType Type of command
     * @param authToken Authentication token
     * @param gameID ID of game to send command to
     */
    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() { return this.commandType; }

    public Integer getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGameCommand that)) return false;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString()) && gameID.equals(that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString(), getGameID());
    }
}
