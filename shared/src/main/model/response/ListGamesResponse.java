package model.response;

import java.util.Collection;

/**
 * A successful response from requesting all games
 */
public class ListGamesResponse extends BaseResponse {

    private final ListGamesObj[] games;

    /**
     * Basic constructor
     * @param games All the games in the database, as a Collection
     */
    public ListGamesResponse(Collection<ListGamesObj> games) {
        super();
        this.games = games.toArray(new ListGamesObj[0]);
    }

    public ListGamesObj[] getGames() {
        return games;
    }
}
