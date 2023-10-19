package model.response;

/**
 * A helper object used solely for listing games
 */
public class ListGamesObj {

    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final boolean isOver;

    /**
     * Basic constructor
     * @param gameID The gameID of this game
     * @param whiteUsername The white player's username, can be null
     * @param blackUsername The black player's username, can be null
     * @param gameName The name of this game
     * @param isOver Whether this game has ended
     */
    public ListGamesObj(int gameID, String whiteUsername, String blackUsername, String gameName, boolean isOver) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.isOver = isOver;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean isOver() {
        return isOver;
    }
}
