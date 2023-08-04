package server;

/**
 * Custom exception used for HTTP 403 errors
 */
public class ForbiddenException extends Exception {

    public ForbiddenException(String message) {
        super("Error: " + message);
    }
}
