package com.t1t.apim.beans.idm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * A role definition.  The definition of the role determines whether the
 * role is automatically granted to the user creating an org, as well as
 * the specific permissions granted to users who are members of organizations
 * via the role.
 */
@Entity
@Table(name = "roles")
public class RoleBean implements Serializable {

    private static final long serialVersionUID = -646534082583069712L;

    @Id
    private String id;
    private String name;
    @Column(updatable = true, nullable = true, length = 512)
    private String description;
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
    @Column(name = "created_on", updatable = false, nullable = false)
    private Date createdOn;
    @Column(name = "auto_grant", updatable = true, nullable = true)
    private Boolean autoGrant = Boolean.FALSE;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "permissions", joinColumns = @JoinColumn(name = "role_id"))
    private Set<PermissionType> permissions;

    /**
     * Constructor.
     */
    public RoleBean() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        RoleBean other = (RoleBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
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
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        return "RoleBean [id=" + id + ", name=" + name + ", description=" + description + ", createdBy="
                + createdBy + ", createdOn=" + createdOn + ", autoGrant=" + autoGrant + ", permissions="
                + permissions + "]";
    }

}
