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
    private String friendlyName;
    private Boolean organizationPrivate;

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

    /**
     * @return the user-friendly name
     */
    public String getFriendlyName() {
        return friendlyName;
    }


    /**
     * @return the organization privacy boolean
     */
    public Boolean isOrganizationPrivate() {
        return organizationPrivate;
    }

    /**
     * @param organizationPrivate the organization privacy to set
     */
    public void setOrganizationPrivate(Boolean organizationPrivate) {
        this.organizationPrivate = organizationPrivate;
    }

    /**
     * @param friendlyName the user-friendly name to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
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
