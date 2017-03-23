package com.t1t.apim.beans.idm;

import java.io.Serializable;

/**
 * @author Maarten Somers
 * @since 2015
 */
public class GrantRoleBean implements Serializable {

    private static final long serialVersionUID = -6287093635002400460L;

    private String userId;
    private String roleId;

    /**
     * Constructor.
     */
    public GrantRoleBean() {
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the role
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "GrantRoleBean [userId=" + userId + ", roleId=" + roleId + "]";
    }

}