package com.t1t.digipolis.apim.beans.idm;

import java.util.Set;

/**
 * Models the currently authenticated user.  This bean extends the simple
 * user bean but also includes all of the user's permissions.
 *
 */
public class CurrentUserBean extends UserBean {

    private static final long serialVersionUID = -5687453720494525865L;

    private Set<PermissionBean> permissions;

    /**
     * Constructor.
     */
    public CurrentUserBean() {
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
     * @param user the user
     */
    public void initFromUser(UserBean user) {
        setEmail(user.getEmail());
        setFullName(user.getFullName());
        setJoinedOn(user.getJoinedOn());
        setUsername(user.getUsername());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "CurrentUserBean [permissions=" + permissions + "]";
    }

}