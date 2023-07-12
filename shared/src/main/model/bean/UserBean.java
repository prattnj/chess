package model.bean;

/**
 * Represents a User entry in the database
 */
public class UserBean {

    private final int userID;
    private final String username;
    private final String password;
    private final String email;

    /**
     * Basic constructor
     * @param userID A unique ID for this user
     * @param username A unique username for this user
     * @param password This user's password
     * @param email This user's email
     */
    public UserBean(int userID, String username, String password, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
