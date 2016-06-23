package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a plugin that does not exist.
 *
 */
public class PluginNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 4877430226993586299L;

    /**
     * Constructor.
     */
    public PluginNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */ 
    public PluginNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param cause the exception cause
     */
    public PluginNotFoundException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public PluginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLUGIN_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLUGIN_NOT_FOUND_INFO;
    }

}
