package com.t1t.digipolis.apim.beans.plans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when creating a plan.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPlanBean implements Serializable {

    private static final long serialVersionUID = 3950418276301140111L;

    private String name;
    private String description;
    private String initialVersion;

    /**
     * Constructor.
     */
    public NewPlanBean() {
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
        return "NewPlanBean [name=" + name + ", description=" + description + ", initialVersion="
                + initialVersion + "]";
    }
}
