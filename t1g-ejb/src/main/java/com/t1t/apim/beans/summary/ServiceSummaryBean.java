package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * A bean modeling a summary of a Service.  Typically used when listing
 * all Services visible to a user.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceSummaryBean implements Serializable, Comparable<ServiceSummaryBean> {

    private static final long serialVersionUID = -4213543715123177461L;

    private String organizationId;
    private String organizationName;
    private String id;
    private String name;
    private String description;
    private Date createdOn;

    /**
     * Constructor.
     */
    public ServiceSummaryBean() {
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
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ServiceSummaryBean other = (ServiceSummaryBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
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
        return "ServiceSummaryBean [organizationId=" + organizationId + ", organizationName="
                + organizationName + ", id=" + id + ", name=" + name + ", description=" + description
                + ", createdOn=" + createdOn + "]";
    }

    @Override
    public int compareTo(ServiceSummaryBean o) {
        int returnValue = 0;
        if (this.equals(o)) returnValue = 0;
        if (this.getId() != null && o.getId() != null) {
            returnValue = this.getId().compareTo(o.getId());
        }
        else {
            if (this.getOrganizationId() != null && o.getOrganizationId() != null) {
                returnValue = this.getOrganizationId().compareTo(o.getOrganizationId());
            }
        }
        return returnValue;
    }
}
