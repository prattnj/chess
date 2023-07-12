package model.request;

/**
 * A request used to register a new user
 */
public class RegisterRequest extends LoginRequest {

    private final String email;

    /**
     * Basic constructor
     * @param username parameter for LoginRequest
     * @param password parameter for LoginRequest
     * @param email The user's email
     */
    public RegisterRequest(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isComplete() {
        return username != null && password != null && email != null;
    }
}
