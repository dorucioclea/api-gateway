package com.t1t.apim.core;

import com.t1t.apim.common.plugin.Plugin;
import com.t1t.apim.common.plugin.PluginCoordinates;
import com.t1t.apim.core.exceptions.InvalidPluginException;

/**
 * The plugin registry used by the API Manager.  The plugin registry provides a way to 
 * download plugins and crack them open to discover features they provide.
 *
 */
public interface IPluginRegistry {
    
    /**
     * Loads a plugin with the given coordinates.
     * @param coordinates plugin coordinates
     * @return the loaded plugin
     * @throws InvalidPluginException the plugin is invalid
     */
    public Plugin loadPlugin(PluginCoordinates coordinates) throws InvalidPluginException;

}
