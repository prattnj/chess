package server;

/**
 * Custom exception used for HTTP 400 errors
 */
public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super("Error: " + message);
    }
}
