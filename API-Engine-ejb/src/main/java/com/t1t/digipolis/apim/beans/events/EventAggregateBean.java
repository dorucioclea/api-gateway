package com.t1t.digipolis.apim.beans.events;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class EventAggregateBean implements Serializable {

    private Long id;
    private String userId;
    private String orgnanizationId;
    private String organizationName;
    private String friendlyName;
    private String applicationOrgId;
    private String applicationOrgName;
    private String applicationOrgFriendlyName;
    private String applicationId;
    private String applicationName;
    private String applicationVersion;
    private String serviceOrgId;
    private String serviceOrgName;
    private String serviceOrgFriendlyName;
    private String serviceId;
    private String serviceName;
    private String serviceVersion;
    private Date createdOn;
    private String planId;
    private String planName;
    private String planVersion;
    private EventType type;
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgnanizationId() {
        return orgnanizationId;
    }

    public void setOrgnanizationId(String orgnanizationId) {
        this.orgnanizationId = orgnanizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getApplicationOrgId() {
        return applicationOrgId;
    }

    public void setApplicationOrgId(String applicationOrgId) {
        this.applicationOrgId = applicationOrgId;
    }

    public String getApplicationOrgName() {
        return applicationOrgName;
    }

    public void setApplicationOrgName(String applicationOrgName) {
        this.applicationOrgName = applicationOrgName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getServiceOrgId() {
        return serviceOrgId;
    }

    public void setServiceOrgId(String serviceOrgId) {
        this.serviceOrgId = serviceOrgId;
    }

    public String getServiceOrgName() {
        return serviceOrgName;
    }

    public void setServiceOrgName(String serviceOrgName) {
        this.serviceOrgName = serviceOrgName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanVersion() {
        return planVersion;
    }

    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getApplicationOrgFriendlyName() {
        return applicationOrgFriendlyName;
    }

    public void setApplicationOrgFriendlyName(String applicationOrgFriendlyName) {
        this.applicationOrgFriendlyName = applicationOrgFriendlyName;
    }

    public String getServiceOrgFriendlyName() {
        return serviceOrgFriendlyName;
    }

    public void setServiceOrgFriendlyName(String serviceOrgFriendlyName) {
        this.serviceOrgFriendlyName = serviceOrgFriendlyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventAggregateBean that = (EventAggregateBean) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EventAggregateBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", orgnanizationId='" + orgnanizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", friendlyName='" + friendlyName + '\'' +
                ", applicationOrgId='" + applicationOrgId + '\'' +
                ", applicationOrgName='" + applicationOrgName + '\'' +
                ", applicationOrgFriendlyName='" + applicationOrgFriendlyName + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", serviceOrgId='" + serviceOrgId + '\'' +
                ", serviceOrgName='" + serviceOrgName + '\'' +
                ", serviceOrgFriendlyName='" + serviceOrgFriendlyName + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", createdOn=" + createdOn +
                ", planId='" + planId + '\'' +
                ", planName='" + planName + '\'' +
                ", planVersion='" + planVersion + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                '}';
    }
}