package com.t1t.apim.beans.members;

import java.io.Serializable;

/**
 * Models a user's membership in a role for a given organization.
 *
 */
public class MemberRoleBean implements Serializable {

    private static final long serialVersionUID = 7645338035552144540L;

    private String roleId;
    private String roleName;

    /**
     * Constructor.
     */
    public MemberRoleBean() {
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "MemberRoleBean [roleId=" + roleId + ", roleName=" + roleName + "]";
    }

}
