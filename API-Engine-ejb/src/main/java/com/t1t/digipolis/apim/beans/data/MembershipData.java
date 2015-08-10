package com.t1t.digipolis.apim.beans.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The data saved along with the audit entry when membership is modified
 * for a user+organization.
 *
 */
public class MembershipData implements Serializable {

    private static final long serialVersionUID = 3424852746654173415L;

    private String userId;
    private Set<String> roles = new HashSet<>();

    /**
     * Constructor.
     */
    public MembershipData() {
    }

    /**
     * @param role the role
     */
    public void addRole(String role) {
        getRoles().add(role);
    }

    /**
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
        final int maxLen = 10;
        return "MembershipData [userId=" + userId + ", roles="
                + (roles != null ? toString(roles, maxLen) : null) + "]";
    }

    @SuppressWarnings("nls")
    private String toString(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

}
