package com.t1t.digipolis.apim.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyDtoBean implements Serializable{

    private Long id;
    private PolicyType type;
    private String organizationId;
    private String entityId;
    private String entityVersion;
    private String name;
    private String description;
    private String configuration;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private PolicyDefinitionBean definition;
    private int orderIndex;
    private String kongPluginId;
    private Long contractId;
    private String gatewayId;
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PolicyType getType() {
        return type;
    }

    public void setType(PolicyType type) {
        this.type = type;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public PolicyDefinitionBean getDefinition() {
        return definition;
    }

    public void setDefinition(PolicyDefinitionBean definition) {
        this.definition = definition;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getKongPluginId() {
        return kongPluginId;
    }

    public void setKongPluginId(String kongPluginId) {
        this.kongPluginId = kongPluginId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolicyDtoBean)) return false;

        PolicyDtoBean that = (PolicyDtoBean) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PolicyDtoBean{" +
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
                ", enabled=" + enabled +
                '}';
    }
}