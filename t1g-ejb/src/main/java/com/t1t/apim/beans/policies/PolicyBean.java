package com.t1t.apim.beans.policies;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @Column(name = "kong_plugin_id", updatable = true, nullable = true)
    private String kongPluginId;
    @Column(name = "contract_id", updatable = true, nullable = true)
    private Long contractId;
    @Column(name = "gateway_id")
    private String gatewayId;
    @Column(name = "enabled")
    private Boolean enabled;

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
     *
     * @return the policyId
     */
    public String getKongPluginId() {
        return kongPluginId;
    }

    /**
     *
     * @param policyId the policyId to set
     */
    public void setKongPluginId(String policyId) {
        this.kongPluginId = policyId;
    }

    /**
     * @return the contract id
     */
    public Long getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contract id to set
     */
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the gateway id
     */
    public String getGatewayId() {
        return gatewayId;
    }

    /**
     * @param gatewayId the gateway id to set
     */
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    /**
     * @return the enabled value
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public String toString() {
        return "PolicyBean{" +
                "id=" + id +
                ", type=" + type +
                ", organizationId='" + organizationId + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityVersion='" + entityVersion + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", configuration='" + configuration + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", definition=" + definition +
                ", orderIndex=" + orderIndex +
                ", kongPluginId='" + kongPluginId + '\'' +
                ", contractId=" + contractId +
                ", gatewayId='" + gatewayId + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }
}
