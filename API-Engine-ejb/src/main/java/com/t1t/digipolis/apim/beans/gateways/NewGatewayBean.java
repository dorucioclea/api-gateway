package com.t1t.digipolis.apim.beans.gateways;

import java.io.Serializable;


/**
 * The bean used when creating a new Gateway.
 *
 */
public class NewGatewayBean implements Serializable {

    private static final long serialVersionUID = -4082669419881570214L;

    private String name;
    private String description;
    private GatewayType type;
    private String configuration;

    /**
     * Constructor.
     */
    public NewGatewayBean() {
    }

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
     * @return the type
     */
    public GatewayType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GatewayType type) {
        this.type = type;
    }

    /**
     * @return the configuration
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewGatewayBean [name=" + name + ", description=" + description + ", type=" + type
                + ", configuration=***]";
    }

}
