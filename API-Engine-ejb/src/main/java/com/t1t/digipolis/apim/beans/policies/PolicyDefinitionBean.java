package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.apim.beans.summary.PolicyFormType;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Policy Definition describes a type of policy that can be added to
 * an application, service, or plan.  A policy cannot be added unless a
 * definition is first created for it.
 *
 */
@Entity
@Table(name = "policydefs")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PolicyDefinitionBean implements Serializable {

    private static final long serialVersionUID = 1801150127602136865L;

    @Id
    @Column(nullable=false)
    private String id;
    @Column(name = "policy_impl", updatable=false, nullable=false)
    private String policyImpl;
    @Column(updatable=true, nullable=false)
    private String name;
    @Column(updatable=true, nullable=false, length=512)
    private String description;
    @Column(updatable=true, nullable=false)
    private String icon;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pd_templates", joinColumns = @JoinColumn(name = "policydef_id"))
    private Set<PolicyDefinitionTemplateBean> templates = new HashSet<>();
    @Column(name = "plugin_id", updatable=false, nullable=true)
    private Long pluginId;
    @Column(name = "form_type", updatable=false, nullable=true)
    @Enumerated(EnumType.STRING)
    private PolicyFormType formType;
    @Column(updatable=false, nullable=true)
    private String form;

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
     * @return the templates
     */
    public Set<PolicyDefinitionTemplateBean> getTemplates() {
        return templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(Set<PolicyDefinitionTemplateBean> templates) {
        this.templates = templates;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final int maxLen = 10;
        return "PolicyDefinitionBean [id=" + id + ", policyImpl=" + policyImpl + ", name=" + name
                + ", description=" + description + ", icon=" + icon + ", templates="
                + (templates != null ? toString(templates, maxLen) : null) + ", pluginId=" + pluginId
                + ", formType=" + formType + ", form=" + form + "]";
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
