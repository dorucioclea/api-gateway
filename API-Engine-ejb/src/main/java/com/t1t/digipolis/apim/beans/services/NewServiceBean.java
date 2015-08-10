package com.t1t.digipolis.apim.beans.services;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when creating a service.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NewServiceBean implements Serializable {

    private static final long serialVersionUID = 8811488441452291116L;

    private String name;
    private String description;
    private String initialVersion;

    /**
     * Constructor.
     */
    public NewServiceBean() {
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
     * @return the initialVersion
     */
    public String getInitialVersion() {
        return initialVersion;
    }

    /**
     * @param initialVersion the initialVersion to set
     */
    public void setInitialVersion(String initialVersion) {
        this.initialVersion = initialVersion;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewServiceBean [name=" + name + ", description=" + description + ", initialVersion="
                + initialVersion + "]";
    }

}
