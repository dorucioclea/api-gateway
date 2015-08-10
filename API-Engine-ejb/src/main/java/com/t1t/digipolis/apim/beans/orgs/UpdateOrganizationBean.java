package com.t1t.digipolis.apim.beans.orgs;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Bean used when updating an organization.
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdateOrganizationBean implements Serializable {

    private static final long serialVersionUID = 2687797041244565943L;

    private String description;

    /**
     * Constructor.
     */
    public UpdateOrganizationBean() {
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
        return "UpdateOrganizationBean [description=" + description + "]";
    }

}
