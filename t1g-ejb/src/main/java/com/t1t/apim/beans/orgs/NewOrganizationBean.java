package com.t1t.apim.beans.orgs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when creating a new organization.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewOrganizationBean implements Serializable {

    private static final long serialVersionUID = 6967624347251134433L;

    private String name;
    private String description;
    private String friendlyName;
    private String optionalTargetContext;
    private Boolean organizationPrivate;

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
     * @return the organization is private Boolean
     */
    public Boolean getOrganizationPrivate() {
        return organizationPrivate;
    }

    /**
     * @param organizationPrivate the Boolean value of whether or not the organization is private
     */
    public void setOrganizationPrivate(Boolean organizationPrivate) {
        this.organizationPrivate = organizationPrivate;
    }

    public String getOptionalTargetContext() {return optionalTargetContext;}

    /**
     * @param optionalTargetContext optional target context, must be a prefix from a managed application
     */
    public void setOptionalTargetContext(String optionalTargetContext) {this.optionalTargetContext = optionalTargetContext;}
}
