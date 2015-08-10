package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when an error happens during publishing (either publishing
 * a Contract or publishing a Service).
 *
 */
public class PublishingException extends AbstractEngineException {

    private static final long serialVersionUID = 3675917769736754387L;
    
    /**
     * Constructor.
     * @param message an error message
     */
    public PublishingException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param message an error message
     * @param cause the exception cause the underlying cause
     */
    public PublishingException(String message, Throwable cause) {
        super(message, cause);
    }

}
