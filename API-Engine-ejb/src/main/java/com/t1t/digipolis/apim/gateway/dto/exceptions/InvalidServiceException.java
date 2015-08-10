package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when attempting to use a Service in some invalid way.  For example
 * when trying to use a Service for one Service when accessing a different Service.
 *
 */
public class InvalidServiceException extends AbstractEngineException {

    private static final long serialVersionUID = 2430774910840954299L;

    /**
     * Constructor.
     * @param message an error message
     */
    public InvalidServiceException(String message) {
        super(message);
    }

}
