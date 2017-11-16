package com.t1t.apim.beans.idm;

import java.io.Serializable;
import java.util.Set;

/**
 * Bean used when creating a new role.
 */
public class NewRoleBean implements Serializable {

    private static final long serialVersionUID = -3926221739472762893L;

    private String name;
    private String description;
    private Boolean autoGrant;
    private Set<PermissionType> permissions;

    /**
     * Constructor.
     */
    public NewRoleBean() {
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
     * @return the permissions
     */
    public Set<PermissionType> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Set<PermissionType> permissions) {
        this.permissions = permissions;
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
     * @return the autoGrant
     */
    public Boolean getAutoGrant() {
        return autoGrant;
    }

    /**
     * @param autoGrant the autoGrant to set
     */
    public void setAutoGrant(Boolean autoGrant) {
        this.autoGrant = autoGrant;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewRoleBean [name=" + name + ", description=" + description + ", autoGrant=" + autoGrant
                + ", permissions=" + permissions + "]";
    }

}
