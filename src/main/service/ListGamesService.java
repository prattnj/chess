package service;

import model.bean.GameBean;
import model.request.BaseRequest;
import model.response.BaseResponse;
import model.response.ListGamesObj;
import model.response.ListGamesResponse;

import java.util.Collection;
import java.util.HashSet;

/**
 * Returns a collection of every game in the database
 */
public class ListGamesService extends Service {

    @Override
    public BaseResponse doService(BaseRequest request, String authToken) throws Exception {
        // request is null, authToken is unused

        Collection<GameBean> allGames = gdao.findAll();
        Collection<ListGamesObj> parsedGames = new HashSet<>();

        for (GameBean game : allGames) {
            String whiteUsername = null;
            String blackUsername = null;
            if (game.getWhitePlayerID() != null) whiteUsername = udao.find(game.getWhitePlayerID()).getUsername();
            if (game.getBlackPlayerID() != null) blackUsername = udao.find(game.getBlackPlayerID()).getUsername();
            parsedGames.add(new ListGamesObj(game.getGameID(), whiteUsername, blackUsername, game.getGameName()));
        }

        return new ListGamesResponse(parsedGames);
    }
}
