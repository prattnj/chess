package model.response;

/**
 * A successful response from creating a game
 */
public class CreateGameResponse extends BaseResponse {

    private final Integer gameID;

    /**
     * Basic constructor
     * @param gameID The gameID of the successfully created game
     */
    public CreateGameResponse(int gameID) {
        super();
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
