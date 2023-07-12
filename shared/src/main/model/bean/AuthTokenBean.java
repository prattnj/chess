package model.bean;

/**
 * Represents an AuthToken entry in the database
 */
public class AuthTokenBean {

    private final String authToken;
    private final int userID;

    /**
     * Basic constructor
     * @param authToken The token itself, as a String
     * @param userID The userID of the user whose authToken this is
     */
    public AuthTokenBean(String authToken, int userID) {
        this.authToken = authToken;
        this.userID = userID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getUserID() {
        return userID;
    }
}
