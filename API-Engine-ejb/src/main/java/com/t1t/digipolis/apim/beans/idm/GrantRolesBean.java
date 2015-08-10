package com.t1t.digipolis.apim.beans.idm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean used to grant role membership for a given user (qualified
 * by organization).
 *
 */
public class GrantRolesBean implements Serializable {

    private static final long serialVersionUID = -1509983712261196134L;

    private String userId;
    private Set<String> roleIds = new HashSet<>();

    /**
     * Constructor.
     */
    public GrantRolesBean() {
    }

    /**
     * @param roleId the role
     */
    public void addRoleId(String roleId) {
        roleIds.add(roleId);
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the roleIds
     */
    public Set<String> getRoleIds() {
        return roleIds;
    }

    /**
     * @param roleIds the roleIds to set
     */
    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleIds() == null) ? 0 : getRoleIds().hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        GrantRolesBean other = (GrantRolesBean) obj;
        if (getRoleIds() == null) {
            if (other.getRoleIds() != null)
                return false;
        } else if (!getRoleIds().equals(other.getRoleIds()))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "GrantRolesBean [userId=" + userId + ", roleIds=" + roleIds + "]";
    }

}
