package com.t1t.apim.common.plugin;

import java.net.URL;
import java.util.List;

/**
 * An apiman plugin.  This represents a plugin that has been downloaded to some local 
 * registry and successfully loaded.  A plugin must contain at least the following
 * file:
 * 
 * META-INF/apiman/plugin.json
 * 
 * It may also contain java classes, resources, and other configuration files.
 * 
 * For example, multiple policy definition files may exist here:
 * 
 * META-INF/apiman/policyDefs/*.json
 *
 */
public class Plugin {
    
    private PluginSpec spec;
    private PluginCoordinates coordinates;
    private PluginClassLoader loader;
    
    /**
     * Constructor.
     * @param spec the plugin's specification
     * @param coordinates the plugin's coordinates
     * @param loader the plugin's classloader
     */
    public Plugin(PluginSpec spec, PluginCoordinates coordinates, PluginClassLoader loader) {
        setSpec(spec);
        setCoordinates(coordinates);
        setLoader(loader);
    }

    /**
     * @return the name
     */
    public String getName() {
        return spec.getName();
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return spec.getDescription();
    }

    /**
     * @return the coordinates
     */
    public PluginCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    protected void setCoordinates(PluginCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return the loader
     */
    public PluginClassLoader getLoader() {
        return loader;
    }

    /**
     * @param loader the loader to set
     */
    protected void setLoader(PluginClassLoader loader) {
        this.loader = loader;
    }

    /**
     * @return the spec
     */
    public PluginSpec getSpec() {
        return spec;
    }

    /**
     * @param spec the spec to set
     */
    public void setSpec(PluginSpec spec) {
        this.spec = spec;
    }

    /**
     * @return any policy definitions found in the plugin (META-INF/apiman/policyDefs/*.json
     */
    public List<URL> getPolicyDefinitions() {
        return this.loader.getPolicyDefinitionResources();
    }

}
