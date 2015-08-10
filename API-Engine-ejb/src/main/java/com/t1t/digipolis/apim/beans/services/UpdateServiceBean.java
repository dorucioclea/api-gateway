package com.t1t.digipolis.apim.beans.services;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when updating a service.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdateServiceBean implements Serializable {

    private static final long serialVersionUID = 8811488441452291116L;

    private String description;

    /**
     * Constructor.
     */
    public UpdateServiceBean() {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdateServiceBean [description=" + description + "]";
    }

}
