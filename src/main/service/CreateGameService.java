package service;

import model.bean.GameBean;
import model.request.BaseRequest;
import model.request.CreateGameRequest;
import model.response.BaseResponse;
import model.response.CreateGameResponse;
import util.Factory;
import util.Util;

/**
 * Creates a new game
 */
public class CreateGameService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // authToken is unused

        CreateGameRequest req = (CreateGameRequest) request;

        GameBean game = new GameBean(Util.getRandomID(5), null, null, req.getGameName(), Factory.getNewGame().toString());
        gdao.insert(game);

        return new CreateGameResponse(game.getGameID());
    }
}
