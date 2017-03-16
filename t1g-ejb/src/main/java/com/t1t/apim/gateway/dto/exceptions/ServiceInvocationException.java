package com.t1t.apim.gateway.dto.exceptions;

/**
 * Exception thrown when there is a problem invoking a back end Service.
 *
 */
public class ServiceInvocationException extends AbstractEngineException {
    
    private static final long serialVersionUID = 4841489368870035902L;

    /**
     * Constructor.
     * @param message an error message
     */
    public ServiceInvocationException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param message an error message
     * @param cause the exception cause the underlying cause
     */
    public ServiceInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

}
