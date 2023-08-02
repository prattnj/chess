package service;

import chess.ChessGame;
import model.bean.GameBean;
import model.request.BaseRequest;
import model.request.JoinGameRequest;
import model.response.BaseResponse;
import server.BadRequestException;
import server.ForbiddenException;

/**
 * Allows the user to join the requested game as a player or viewer
 */
public class JoinGameService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {

        JoinGameRequest req = (JoinGameRequest) request;

        int userID = adao.find(authToken).getUserID();

        GameBean game = gdao.find(req.getGameID());

        // Verify that this game exists
        if (game == null) throw new BadRequestException("invalid game ID");

        // Verify that this team color is valid
        ChessGame.TeamColor color = null;
        if (req.getPlayerColor() != null) {
            if (req.getPlayerColor().equalsIgnoreCase("white")) color = ChessGame.TeamColor.WHITE;
            else if (req.getPlayerColor().equalsIgnoreCase("black")) color = ChessGame.TeamColor.BLACK;
            else throw new BadRequestException("invalid team color");
        }

        // Verify that this team color isn't taken
        Exception ex = new ForbiddenException("color is taken");
        if (color == ChessGame.TeamColor.WHITE && game.getWhitePlayerID() != null && game.getWhitePlayerID() != userID) throw ex;
        else if (color == ChessGame.TeamColor.BLACK && game.getBlackPlayerID() != null && game.getBlackPlayerID() != userID) throw ex;

        // This is a valid request
        if (color != null) {
            if (color == ChessGame.TeamColor.WHITE) game.setWhitePlayerID(userID);
            else game.setBlackPlayerID(userID);
        }
        gdao.claimSpot(game.getGameID(), color, userID);

        return new BaseResponse();
    }
}
