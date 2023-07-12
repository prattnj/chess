package model.response;

/**
 * A bare-bones response sent from the server to the client
 */
public class BaseResponse {

    private final boolean success;
    private final String message;

    /**
     * The success case constructor
     */
    public BaseResponse() {
        success = true;
        message = null;
    }

    /**
     * The fail case constructor
     * @param message A String describing why this action failed
     */
    public BaseResponse(String message) {
        success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
