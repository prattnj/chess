package model.wsMessage.userCommand;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerUC extends UserGameCommand {

    private final ChessGame.TeamColor playerColor;

    /**
     * Basic constructor
     * @param authToken The auth token of the user joining the game
     * @param gameID The gameID of the game to join
     * @param playerColor The color the player wants to play as
     */
    public JoinPlayerUC(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
