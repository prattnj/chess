package service;

import model.bean.GameBean;
import model.request.BaseRequest;
import model.request.CreateGameRequest;
import model.response.BaseResponse;
import model.response.CreateGameResponse;
import server.ForbiddenException;
import util.Factory;
import util.Util;

import java.util.Collection;

/**
 * Creates a new game
 */
public class CreateGameService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // authToken is unused

        CreateGameRequest req = (CreateGameRequest) request;

        Collection<GameBean> allGames = gdao.findAll();
        if (allGames.size() >= 1000) throw new ForbiddenException("game limit reached");

        int potentialID = Util.getRandomID(5);
        while (gamesContainsID(allGames, potentialID)) potentialID = Util.getRandomID(5);

        GameBean game = new GameBean(potentialID, null, null, req.getGameName(), Factory.getNewGame().toString());
        gdao.insert(game);

        return new CreateGameResponse(game.getGameID());
    }

    private boolean gamesContainsID(Collection<GameBean> games, int id) {
        for (GameBean g : games) if (g.getGameID() == id) return true;
        return false;
    }
}
