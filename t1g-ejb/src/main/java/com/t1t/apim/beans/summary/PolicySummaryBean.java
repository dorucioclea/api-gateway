package com.t1t.apim.beans.summary;

import java.io.Serializable;
import java.util.Date;

/**
 * Summary of policy info.
 */
public class PolicySummaryBean implements Serializable {

    private static final long serialVersionUID = 1208106756423327108L;

    private String policyDefinitionId;
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String createdBy;
    private Date createdOn;

    /**
     * @return the policyDefinitionId
     */
    public String getPolicyDefinitionId() {
        return policyDefinitionId;
    }

    /**
     * @param policyDefinitionId the policyDefinitionId to set
     */
    public void setPolicyDefinitionId(String policyDefinitionId) {
        this.policyDefinitionId = policyDefinitionId;
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
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "PolicySummaryBean [policyDefinitionId=" + policyDefinitionId + ", id=" + id + ", name="
                + name + ", description=" + description + ", icon=" + icon + ", createdBy=" + createdBy
                + ", createdOn=" + createdOn + "]";
    }

}
