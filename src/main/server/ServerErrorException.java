package server;

import static util.Util.SERVER_ERROR;

/**
 * Custom exception used for HTTP 500 errors
 */
public class ServerErrorException extends Exception {

    public ServerErrorException() {
        super(SERVER_ERROR);
    }

}
