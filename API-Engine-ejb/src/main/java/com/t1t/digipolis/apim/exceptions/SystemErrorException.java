package com.t1t.digipolis.apim.exceptions;

/**
 * Thrown when something unexpected happens.
 */
public class SystemErrorException extends AbstractSystemException {
    
    private static final long serialVersionUID = 5590264580639703192L;
    
    /**
     * Constructor.
     */
    public SystemErrorException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public SystemErrorException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param t the cause t
     */
    public SystemErrorException(Throwable t) {
        super(t);
    }
    
    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return -1;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return null;
    }

}
