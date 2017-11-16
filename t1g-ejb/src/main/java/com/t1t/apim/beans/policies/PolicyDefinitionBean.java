package com.t1t.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.summary.PolicyFormType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Policy Definition describes a type of policy that can be added to
 * an application, service, or plan.  A policy cannot be added unless a
 * definition is first created for it.
 */
@Entity
@Table(name = "policydefs")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyDefinitionBean implements Serializable {

    private static final long serialVersionUID = 1801150127602136865L;

    @Id
    @Column(nullable = false)
    private String id;
    @Column(updatable = true, nullable = false)
    private String name;
    @Column(updatable = true, nullable = false, length = 512)
    private String description;
    @Column(updatable = true, nullable = false)
    private String icon;
    @Column(name = "plugin_id", updatable = false, nullable = true)
    private Long pluginId;
    @Column(name = "form_type", updatable = false, nullable = true)
    @Enumerated(EnumType.STRING)
    private PolicyFormType formType;
    @Column(updatable = false, nullable = true)
    private String form;
    @Column(name = "scope_service")
    private Boolean scopeService;
    @Column(name = "scope_plan")
    private Boolean scopePlan;
    @Column(name = "scope_auto")
    private Boolean scopeAuto;
    @Column(name = "form_override")
    private String formOverride;
    @Column(name = "default_config")
    private String defaultConfig;
    @Lob
    @Column(name = "logo")
    @Type(type = "org.hibernate.type.TextType")
    private String base64logo;
    @Column(name = "marketplace_description", length = 4096)
    private String marketplaceDescription;
    @Column(name = "popover_template", length = 4096)
    private String popoverTemplate;

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
     * @return the scope service value
     */
    public Boolean getScopeService() {
        return scopeService;
    }

    /**
     * @param scopeService the scope service value to set
     */
    public void setScopeService(Boolean scopeService) {
        this.scopeService = scopeService;
    }

    /**
     * @return the scope plan value
     */
    public Boolean getScopePlan() {
        return scopePlan;
    }

    /**
     * @param scopePlan the scope plan value to set
     */
    public void setScopePlan(Boolean scopePlan) {
        this.scopePlan = scopePlan;
    }

    /**
     * @return the scope auto value
     */
    public Boolean getScopeAuto() {
        return scopeAuto;
    }

    /**
     * @param scopeAuto the scope auto value to set
     */
    public void setScopeAuto(Boolean scopeAuto) {
        this.scopeAuto = scopeAuto;
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
     * @return the default config
     */
    public String getDefaultConfig() {
        return defaultConfig;
    }

    /**
     * @param defaultConfig the default config to set
     */
    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    /**
     * @return the base64-encoded logo
     */
    public String getBase64logo() {
        return base64logo;
    }

    /**
     * @param base64logo the base64-encoded logo to set
     */
    public void setBase64logo(String base64logo) {
        this.base64logo = base64logo;
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
     * @return the popover template
     */
    public String getPopoverTemplate() {
        return popoverTemplate;
    }

    /**
     * @param popoverTemplate the popover template to set
     */
    public void setPopoverTemplate(String popoverTemplate) {
        this.popoverTemplate = popoverTemplate;
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
        PolicyDefinitionBean other = (PolicyDefinitionBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PolicyDefinitionBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", pluginId=" + pluginId +
                ", formType=" + formType +
                ", form='" + form + '\'' +
                ", scopeService=" + scopeService +
                ", scopePlan=" + scopePlan +
                ", scopeAuto=" + scopeAuto +
                ", formOverride='" + formOverride + '\'' +
                ", defaultConfig='" + defaultConfig + '\'' +
                ", base64logo='" + base64logo + '\'' +
                ", marketplaceDescription='" + marketplaceDescription + '\'' +
                ", popoverTemplate='" + popoverTemplate + '\'' +
                '}';
    }
}
