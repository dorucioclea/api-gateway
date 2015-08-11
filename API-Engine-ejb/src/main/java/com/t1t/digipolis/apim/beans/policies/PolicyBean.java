package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.apim.common.util.AesEncrypter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A Policy is the primary unit of work for the runtime engine, which is
 * essentially a chain of policies that are applied to the Request and
 * Response of a service.
 *
 */
@Entity
@Table(name = "policies")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PolicyBean implements Serializable {

    private static final long serialVersionUID = -8534463608508756791L;

    @Id @GeneratedValue
    private Long id;
    @Column(updatable=false, nullable=false)
    @Enumerated(EnumType.STRING)
    private PolicyType type;
    @Column(name = "organization_id", updatable=false, nullable=false)
    private String organizationId;
    @Column(name = "entity_id", updatable=false, nullable=false)
    private String entityId;
    @Column(name = "entity_version", updatable=false, nullable=false)
    private String entityVersion;
    @Column(updatable=true, nullable=false)
    private String name;
    // description is generated using MVEL
    @Transient
    private String description;
    @Lob
    @Column(updatable=true, nullable=true)
    @Type(type = "org.hibernate.type.TextType")
    private String configuration;
    @Column(name = "created_by", updatable=false, nullable=false)
    private String createdBy;
    @Column(name = "created_on", updatable=false, nullable=false)
    private Date createdOn;
    @Column(name = "modified_by", updatable=true, nullable=false)
    private String modifiedBy;
    @Column(name = "modified_on", updatable=true, nullable=false)
    private Date modifiedOn;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    private PolicyDefinitionBean definition;
    @Column(name = "order_index", updatable=true, nullable=false)
    private int orderIndex;

    /**
     * Constructor.
     */
    public PolicyBean() {
    }

    @PrePersist @PreUpdate
    protected void encryptData() {
        // Encrypt the endpoint properties.
        configuration = AesEncrypter.encrypt(configuration);
    }

    @PostPersist @PostUpdate @PostLoad
    protected void decryptData() {
        // Encrypt the endpoint properties.
        configuration = AesEncrypter.decrypt(configuration);
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
     * @return the type
     */
    public PolicyType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(PolicyType type) {
        this.type = type;
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
     * @return the entityId
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the configuration
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the entityVersion
     */
    public String getEntityVersion() {
        return entityVersion;
    }

    /**
     * @param entityVersion the entityVersion to set
     */
    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
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
     * @return the definition
     */
    public PolicyDefinitionBean getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(PolicyDefinitionBean definition) {
        this.definition = definition;
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
     * @return the orderIndex
     */
    public int getOrderIndex() {
        return orderIndex;
    }

    /**
     * @param orderIndex the orderIndex to set
     */
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
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
        PolicyBean other = (PolicyBean) obj;
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
        return "PolicyBean [id=" + id + ", type=" + type + ", organizationId=" + organizationId
                + ", entityId=" + entityId + ", entityVersion=" + entityVersion + ", name=" + name
                + ", description=" + description + ", configuration=***, createdBy="
                + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn="
                + modifiedOn + ", definition=" + definition + ", orderIndex=" + orderIndex + "]";
    }

}