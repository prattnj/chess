package model.request;

/**
 * Any parseable object sent to this server
 */
public abstract class BaseRequest {

    /**
     * Used by the handlers to verify if this request is missing any parameters
     * @return whether this request is ready to be used
     */
    public abstract boolean isComplete();

}
