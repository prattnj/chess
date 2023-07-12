package model.request;

/**
 * A request used to log in
 */
public class LoginRequest extends BaseRequest {

    protected final String username;
    protected final String password;

    /**
     * Basic constructor
     * @param username The login attempt's username
     * @param password The login attempt's password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isComplete() {
        return username != null && password != null;
    }
}
