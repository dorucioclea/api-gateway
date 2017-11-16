package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.apps.ApplicationStatus;

import java.io.Serializable;


/**
 * A summary of an individual application version.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationVersionSummaryBean implements Serializable {

    private String organizationId;
    private String organizationName;
    private String id;
    private String name;
    private String description;
    private ApplicationStatus status;
    private String version;
    private String kongConsumerId;

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
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
     * @return the status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the Kong consumer ID
     */
    public String getKongConsumerId() {
        return kongConsumerId;
    }

    /**
     * @param kongConsumerId the Kong consumer ID to set
     */
    public void setKongConsumerId(String kongConsumerId) {
        this.kongConsumerId = kongConsumerId;
    }

    @Override
    public String toString() {
        return "ApplicationVersionSummaryBean{" +
                "organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", version='" + version + '\'' +
                ", kongConsumerId='" + kongConsumerId + '\'' +
                '}';
    }
}
