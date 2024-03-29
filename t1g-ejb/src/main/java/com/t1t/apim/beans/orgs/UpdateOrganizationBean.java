package com.t1t.apim.beans.orgs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when updating an organization.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
     * @param friendlyName the user-friendly name to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
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

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdateOrganizationBean [description=" + description + "]";
    }

}
