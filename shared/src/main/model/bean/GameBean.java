package model.bean;

/**
 * Represents a Game entry in the database
 */
public class GameBean {

    private final int gameID;
    private Integer whitePlayerID;
    private Integer blackPlayerID;
    private final String gameName;
    private final String game;

    /**
     * Basic constructor
     * @param gameID A unique ID for this game
     * @param whitePlayerID The userID of the white player (can be null)
     * @param blackPlayerID The userID of the black player (can be null)
     * @param gameName A unique name for this game
     * @param game A string representation of this game
     */
    public GameBean(int gameID, Integer whitePlayerID, Integer blackPlayerID, String gameName, String game) {
        this.gameID = gameID;
        this.whitePlayerID = whitePlayerID;
        this.blackPlayerID = blackPlayerID;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public Integer getWhitePlayerID() {
        return whitePlayerID;
    }

    public Integer getBlackPlayerID() {
        return blackPlayerID;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGame() {
        return game;
    }

    public void setWhitePlayerID(int whitePlayerID) {
        this.whitePlayerID = whitePlayerID;
    }

    public void setBlackPlayerID(int blackPlayerID) {
        this.blackPlayerID = blackPlayerID;
    }
}
