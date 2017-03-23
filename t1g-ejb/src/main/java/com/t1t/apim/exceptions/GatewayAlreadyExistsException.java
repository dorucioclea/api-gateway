package com.t1t.apim.exceptions;


/**
 * Thrown when trying to create a Gateway that already exists.
 *
 */
public class GatewayAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -6073501704605940260L;

    /**
     * Constructor.
     */
    public GatewayAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public GatewayAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.GATEWAY_ALREADY_EXISTS;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.GATEWAY_ALREADY_EXISTS_INFO;
    }

}
