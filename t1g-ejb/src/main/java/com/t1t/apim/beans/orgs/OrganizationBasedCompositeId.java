package com.t1t.apim.beans.orgs;

import java.io.Serializable;

/**
 * Composite key for entities that are owned by an organization.
 *
 */
public class OrganizationBasedCompositeId implements Serializable {

    private static final long serialVersionUID = 7313295981342740517L;

    private OrganizationBean organization;
    private String id;

    /**
     * Constructor.
     */
    public OrganizationBasedCompositeId() {
    }

    /**
     * Constructor.
     * @param organization the organization
     * @param id the id
     */
    public OrganizationBasedCompositeId(OrganizationBean organization, String id) {
        this.setOrganization(organization);
        this.setId(id);
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
     * @return the organization
     */
    public OrganizationBean getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((organization == null) ? 0 : organization.getId().hashCode());
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
        OrganizationBasedCompositeId other = (OrganizationBasedCompositeId) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (organization == null) {
            if (other.organization != null)
                return false;
        } else if (!organization.getId().equals(other.organization.getId()))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "OrganizationBasedCompositeId [organization=" + organization + ", id=" + id + "]";
    }

}
