package model.wsMessage.userCommand;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerUC extends UserGameCommand {

    private final ChessGame.TeamColor playerColor;

    public JoinPlayerUC(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
