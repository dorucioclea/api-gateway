package com.t1t.digipolis.apim.beans.apps;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when updating an application.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdateApplicationBean implements Serializable {

    private static final long serialVersionUID = 5549391329361400489L;

    private String description;

    /**
     * Constructor.
     */
    public UpdateApplicationBean() {
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
        return "UpdateApplicationBean [description=" + description + "]";
    }

}
