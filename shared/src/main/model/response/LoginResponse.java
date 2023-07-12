package model.response;

/**
 * A successful response from logging in
 */
public class LoginResponse extends BaseResponse {

    private final String authToken;
    private final String username;

    /**
     * Basic constructor
     * @param authToken The logged-in user's authorization token
     * @param username The logged-in user's username
     */
    public LoginResponse(String authToken, String username) {
        super();
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
