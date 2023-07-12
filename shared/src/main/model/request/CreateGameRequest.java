package model.request;

/**
 * A request used to create a game
 */
public class CreateGameRequest extends BaseRequest {

    private final String gameName;

    /**
     * Basic constructor
     * @param gameName The name of the game to be created
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    @Override
    public boolean isComplete() {
        return gameName != null && !gameName.equals("");
    }
}
