package com.t1t.apim.beans.idm;

import java.io.Serializable;
import java.util.Set;

/**
 * All of a user's permissions.
 */
public class UserPermissionsBean implements Serializable {

    private static final long serialVersionUID = -3148877050712405169L;

    private String userId;
    private Set<PermissionBean> permissions;

    /**
     * Constructor.
     */
    public UserPermissionsBean() {
    }

    /**
     * @return the permissions
     */
    public Set<PermissionBean> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Set<PermissionBean> permissions) {
        this.permissions = permissions;
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
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
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
        UserPermissionsBean other = (UserPermissionsBean) obj;
        if (permissions == null) {
            if (other.permissions != null)
                return false;
        } else if (!permissions.equals(other.permissions))
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
        return "UserPermissionsBean [userId=" + userId + ", permissions=" + permissions + "]";
    }
}
