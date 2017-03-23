package com.t1t.apim.common.plugin;

/**
 * The plugin spec.
 *
 */
public class PluginSpec {
    
    private double frameworkVersion = 1.0;
    private String name;
    private String description;
    private String version;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the frameworkVersion
     */
    public double getFrameworkVersion() {
        return frameworkVersion;
    }

    /**
     * @param frameworkVersion the frameworkVersion to set
     */
    public void setFrameworkVersion(double frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }
}
