package com.t1t.digipolis.apim.beans.plans;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when creating a plan.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdatePlanBean implements Serializable {

    private static final long serialVersionUID = 5836879095486293752L;

    private String description;

    /**
     * Constructor.
     */
    public UpdatePlanBean() {
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

}
