package com.t1t.digipolis.apim.beans.summary;

import java.io.Serializable;

/**
 * A summary of a policy definition.
 *
 */
public class PolicyDefinitionSummaryBean implements Serializable {

    private static final long serialVersionUID = 6297595620199835022L;

    private String id;
    private String policyImpl;
    private String name;
    private String description;
    private String icon;
    private PolicyFormType formType;
    private Boolean scopeService;
    private Boolean scopePlan;
    private Boolean scopeAuto;
    private Long pluginId;

    /**
     * Constructor.
     */
    public PolicyDefinitionSummaryBean() {
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
     * @return the policyImpl
     */
    public String getPolicyImpl() {
        return policyImpl;
    }

    /**
     * @param policyImpl the policyImpl to set
     */
    public void setPolicyImpl(String policyImpl) {
        this.policyImpl = policyImpl;
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
     * @return the pluginId
     */
    public Long getPluginId() {
        return pluginId;
    }

    /**
     * @param pluginId the pluginId to set
     */
    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * @return the formType
     */
    public PolicyFormType getFormType() {
        return formType;
    }

    /**
     * @param formType the formType to set
     */
    public void setFormType(PolicyFormType formType) {
        this.formType = formType;
    }

    public Boolean getScopeService() {
        return scopeService;
    }

    public void setScopeService(Boolean scopeService) {
        this.scopeService = scopeService;
    }

    public Boolean getScopePlan() {
        return scopePlan;
    }

    public void setScopePlan(Boolean scopePlan) {
        this.scopePlan = scopePlan;
    }

    public Boolean getScopeAuto() {
        return scopeAuto;
    }

    public void setScopeAuto(Boolean scopeAuto) {
        this.scopeAuto = scopeAuto;
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
        PolicyDefinitionSummaryBean other = (PolicyDefinitionSummaryBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PolicyDefinitionSummaryBean{" +
                "id='" + id + '\'' +
                ", policyImpl='" + policyImpl + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", formType=" + formType +
                ", scopeService=" + scopeService +
                ", scopePlan=" + scopePlan +
                ", scopeAuto=" + scopeAuto +
                ", pluginId=" + pluginId +
                '}';
    }
}
