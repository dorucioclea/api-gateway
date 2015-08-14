package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to get, update, or remove a gateay that does not exist.
 *
 */
public class GatewayNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 6854316462415238451L;

    /**
     * Constructor.
     */
    public GatewayNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public GatewayNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.GATEWAY_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.GATEWAY_NOT_FOUND_INFO;
    }

}
