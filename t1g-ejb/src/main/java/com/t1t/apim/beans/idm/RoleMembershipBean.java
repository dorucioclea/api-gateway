package com.t1t.apim.beans.idm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A single, qualified, role granted to the user.  Roles in the system
 * might include things like "Service Owner", "Application Developer", etc.
 * A role is qualified by an Organization ID.  The purpose of a role is
 * to grant permissions to a user.  A role might grant CREATE-APPLICATION
 * and VIEW-SERVICE permissions for a particular Organization.
 *
 */
@Entity
@Table(name = "memberships",
       uniqueConstraints={@UniqueConstraint(columnNames={"user_id","role_id","org_id"})})
public class RoleMembershipBean implements Serializable {

    private static final long serialVersionUID = 7798709783947356888L;

    @Id @GeneratedValue
    private Long id;
    @Column(name="user_id")
    private String userId;
    @Column(name="role_id")
    private String roleId;
    @Column(name="org_id")
    private String organizationId;
    @Column(name = "created_on")
    private Date createdOn;

    public static final RoleMembershipBean create(String userId, String roleId, String organizationId) {
        RoleMembershipBean bean = new RoleMembershipBean();
        bean.setUserId(userId);
        bean.setRoleId(roleId);
        bean.setOrganizationId(organizationId);
        return bean;
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
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
        RoleMembershipBean other = (RoleMembershipBean) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "RoleMembershipBean [id=" + id + ", userId=" + userId + ", roleId=" + roleId
                + ", organizationId=" + organizationId + ", createdOn=" + createdOn + "]";
    }

}
