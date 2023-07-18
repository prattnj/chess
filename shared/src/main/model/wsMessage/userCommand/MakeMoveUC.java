package model.wsMessage.userCommand;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMoveUC extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveUC(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
