package com.t1t.digipolis.apim.beans.gateways;

import java.io.Serializable;


/**
 * The bean used when updating an existing Gateway.
 *
 */
public class UpdateGatewayBean implements Serializable {

    private static final long serialVersionUID = 4301970694825472031L;

    private String description;
    private GatewayType type;
    private String configuration;

    /**
     * Constructor.
     */
    public UpdateGatewayBean() {
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
        return "UpdateGatewayBean [description=" + description + ", type=" + type + ", configuration=***]";
    }

}
