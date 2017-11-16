package com.t1t.apim.beans.idm;

import java.io.Serializable;

/**
 * Models a single qualified permission assigned to a user.  All permissions
 * are granted to users by membership in one or more roles.  Membership in a
 * role is qualified by organization, allowing a user to have different roles
 * in different organizations.
 */
public class PermissionBean implements Serializable {

    private static final long serialVersionUID = 6005936454144731711L;

    private PermissionType name;
    private String organizationId;

    /**
     * Constructor.
     */
    public PermissionBean() {
    }

    /**
     * @return the name
     */
    public PermissionType getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(PermissionType name) {
        this.name = name;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PermissionBean other = (PermissionBean) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (organizationId == null) {
            if (other.organizationId != null)
                return false;
        } else if (!organizationId.equals(other.organizationId))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "PermissionBean [name=" + name + ", organizationId=" + organizationId + "]";
    }

}
