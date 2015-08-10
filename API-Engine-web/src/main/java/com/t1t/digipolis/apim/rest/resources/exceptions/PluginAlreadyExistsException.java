package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to create a Gateway that already exists.
 *
 */
public class PluginAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -4273634641752566385L;

    /**
     * Constructor.
     */
    public PluginAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PluginAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLUGIN_ALREADY_EXISTS;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLUGIN_ALREADY_EXISTS_INFO;
    }

}
