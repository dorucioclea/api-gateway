package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.policies.PolicyType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrichedPolicySummaryBean implements Serializable {

    private Long id;
    private PolicyType type;
    private String organizationId;
    private String entityId;
    private String entityVersion;
    private PolicyDefinitionSummaryBean definition;
    private String configuration;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private Integer orderIndex;
    private String kongPluginId;
    private Long contractId;
    private String gatewayId;
    private Boolean enabled;
    private String popover;

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

    public PolicyDefinitionSummaryBean getDefinition() {
        return definition;
    }

    public void setDefinition(PolicyDefinitionSummaryBean definition) {
        this.definition = definition;
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

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
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

    public String getPopover() {
        return popover;
    }

    public void setPopover(String popover) {
        this.popover = popover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrichedPolicySummaryBean)) return false;

        EnrichedPolicySummaryBean that = (EnrichedPolicySummaryBean) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EnrichedPolicySummaryBean{" +
                "id=" + id +
                ", type=" + type +
                ", organizationId='" + organizationId + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityVersion='" + entityVersion + '\'' +
                ", definition=" + definition +
                ", configuration='" + configuration + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedOn=" + modifiedOn +
                ", orderIndex=" + orderIndex +
                ", kongPluginId='" + kongPluginId + '\'' +
                ", contractId=" + contractId +
                ", gatewayId='" + gatewayId + '\'' +
                ", enabled=" + enabled +
                ", popover='" + popover + '\'' +
                '}';
    }
}