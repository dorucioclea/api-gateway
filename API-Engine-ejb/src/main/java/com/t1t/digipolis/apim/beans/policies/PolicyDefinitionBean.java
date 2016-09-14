package com.t1t.digipolis.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.digipolis.apim.beans.summary.PolicyFormType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * A Policy Definition describes a type of policy that can be added to
 * an application, service, or plan.  A policy cannot be added unless a
 * definition is first created for it.
 *
 */
@Entity
@Table(name = "policydefs")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyDefinitionBean implements Serializable {

    private static final long serialVersionUID = 1801150127602136865L;

    @Id
    @Column(nullable=false)
    private String id;
    @Column(updatable=true, nullable=false)
    private String name;
    @Column(updatable=true, nullable=false, length=512)
    private String description;
    @Column(updatable=true, nullable=false)
    private String icon;
    @Column(name = "plugin_id", updatable=false, nullable=true)
    private Long pluginId;
    @Column(name = "form_type", updatable=false, nullable=true)
    @Enumerated(EnumType.STRING)
    private PolicyFormType formType;
    @Column(updatable=false, nullable=true)
    private String form;
    @Column(name="scope_service")
    private Boolean scopeService;
    @Column(name="scope_plan")
    private Boolean scopePlan;
    @Column(name="scope_auto")
    private Boolean scopeAuto;

    /**
     * Constructor.
     */
    public PolicyDefinitionBean() {
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
                '}';
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
