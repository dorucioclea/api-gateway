package com.t1t.digipolis.apim.beans.summary;


import com.t1t.digipolis.apim.beans.gateways.GatewayType;

import java.io.Serializable;

/**
 * A summary bean for {@link com.t1t.digipolis.apim.beans.gateways.GatewayBean}.
 *
 */
public class GatewaySummaryBean implements Serializable {

    private static final long serialVersionUID = 2781650785786597040L;

    private String id;
    private String name;
    private String description;
    private GatewayType type;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "GatewaySummaryBean [id=" + id + ", name=" + name + ", description=" + description + ", type="
                + type + "]";
    }

}
