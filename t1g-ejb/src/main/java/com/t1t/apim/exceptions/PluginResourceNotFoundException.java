package com.t1t.apim.exceptions;


/**
 * Thrown when trying to get a resource from a plugin.
 *
 */
public class PluginResourceNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 2620584248905679316L;

    /**
     * Constructor.
     */
    public PluginResourceNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PluginResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLUGIN_RESOURCE_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLUGIN_RESOURCE_NOT_FOUND_INFO;
    }

}
