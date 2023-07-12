package model.request;

/**
 * A request used to join a game, either as a player or a viewer
 */
public class JoinGameRequest extends BaseRequest {

    private final String playerColor;
    private final int gameID;

    /**
     * Basic constructor
     * @param playerColor The color that this user wants to play as
     * @param gameID The game that the user is attempting to join
     */
    public JoinGameRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
