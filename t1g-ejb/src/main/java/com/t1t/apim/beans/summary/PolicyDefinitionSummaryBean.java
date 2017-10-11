package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * A summary of a policy definition.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyDefinitionSummaryBean implements Serializable {

    private String id;
    private String name;
    private String description;
    private String marketplaceDescription;
    private String icon;
    private PolicyFormType formType;
    private String form;
    private String formOverride;
    private Boolean scopeService;
    private Boolean scopePlan;
    private Boolean scopeAuto;
    private String base64Logo;

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

    /**
     * @return the service scoped value
     */
    public Boolean getScopeService() {
        return scopeService;
    }

    /**
     * @param scopeService the service scoped value to set
     */
    public void setScopeService(Boolean scopeService) {
        this.scopeService = scopeService;
    }

    /**
     * @return the plan scoped value
     */
    public Boolean getScopePlan() {
        return scopePlan;
    }

    /**
     * @param scopePlan the plan scoped value to set
     */
    public void setScopePlan(Boolean scopePlan) {
        this.scopePlan = scopePlan;
    }

    /**
     * @return the auto scoped value
     */
    public Boolean getScopeAuto() {
        return scopeAuto;
    }

    /**
     * @param scopeAuto the auto scoped value to set
     */
    public void setScopeAuto(Boolean scopeAuto) {
        this.scopeAuto = scopeAuto;
    }

    /**
     * @return the Base64 encoded logo String
     */
    public String getBase64Logo() {
        return base64Logo;
    }

    /**
     * @param base64Logo the Base64 encoded logo String to set
     */
    public void setBase64Logo(String base64Logo) {
        this.base64Logo = base64Logo;
    }

    /**
     * @return the marketplace description
     */
    public String getMarketplaceDescription() {
        return marketplaceDescription;
    }

    /**
     * @param marketplaceDescription the marketplace description to set
     */
    public void setMarketplaceDescription(String marketplaceDescription) {
        this.marketplaceDescription = marketplaceDescription;
    }

    /**
     * @return the form
     */
    public String getForm() {
        return form;
    }

    /**
     * @param form the form to set
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     * @return the form override
     */
    public String getFormOverride() {
        return formOverride;
    }

    /**
     * @param formOverride the form override to set
     */
    public void setFormOverride(String formOverride) {
        this.formOverride = formOverride;
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", marketplaceDescription='" + marketplaceDescription + '\'' +
                ", icon='" + icon + '\'' +
                ", formType=" + formType +
                ", form='" + form + '\'' +
                ", formOverride='" + formOverride + '\'' +
                ", scopeService=" + scopeService +
                ", scopePlan=" + scopePlan +
                ", scopeAuto=" + scopeAuto +
                ", base64Logo='" + base64Logo + '\'' +
                '}';
    }
}
