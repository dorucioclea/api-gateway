package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * A bean modeling a summary of an Application.  Typically used when listing
 * all Applications visible to a user.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationSummaryBean implements Serializable {

    private static final long serialVersionUID = -4213543715123177461L;

    private String organizationId;
    private String organizationName;
    private String id;
    private String name;
    private String description;
    private int numContracts;
    private String base64logo;

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
     * @return the numContracts
     */
    public int getNumContracts() {
        return numContracts;
    }

    /**
     * @param numContracts the numContracts to set
     */
    public void setNumContracts(int numContracts) {
        this.numContracts = numContracts;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getBase64logo() {
        return base64logo;
    }

    public void setBase64logo(String base64logo) {
        this.base64logo = base64logo;
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
        ApplicationSummaryBean other = (ApplicationSummaryBean) obj;
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

    @Override
    public String toString() {
        return "ApplicationSummaryBean{" +
                "organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", numContracts=" + numContracts +
                ", base64logo='" + base64logo + '\'' +
                '}';
    }

}
