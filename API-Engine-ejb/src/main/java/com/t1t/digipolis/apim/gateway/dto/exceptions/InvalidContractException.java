package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when attempting to use a Contract in some invalid way.  For example
 * when trying to use a Contract for one Service when accessing a different Service.
 *
 */
public class InvalidContractException extends AbstractEngineException {
    
    private static final long serialVersionUID = -378275941461121749L;

    /**
     * Constructor.
     * @param message an error message
     */
    public InvalidContractException(String message) {
        super(message);
    }

}
