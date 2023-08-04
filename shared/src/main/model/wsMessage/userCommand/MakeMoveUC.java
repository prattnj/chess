package model.wsMessage.userCommand;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMoveUC extends UserGameCommand {

    private final ChessMove move;

    /**
     * Basic constructor
     * @param authToken The auth token of the user making the move
     * @param gameID The gameID of the game to make the move in
     * @param move The move to make
     */
    public MakeMoveUC(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
