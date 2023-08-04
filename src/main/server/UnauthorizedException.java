package server;

/**
 * Custom exception used for HTTP 401 errors
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super("Error: " + message);
    }
}
