package com.t1t.apim.beans.system;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * A simple bean used to return system status information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemStatusBean implements Serializable {

    private static final long serialVersionUID = -7099876167335105162L;

    private String id;
    private String name;
    private String description;
    private String moreInfo;
    private String version;
    private String environment;
    private String builtOn;
    private String kongInfo;
    private String kongStatus;
    private String kongCluster;
    private Boolean maintenanceModeEnabled;
    private String maintenanceMessage;
    private boolean up;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
     * @return the up
     */
    public boolean isUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public void setUp(boolean up) {
        this.up = up;
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
     * @return the builtOn
     */
    public String getBuiltOn() {
        return builtOn;
    }

    /**
     * @param builtOn the builtOn to set
     */
    public void setBuiltOn(String builtOn) {
        this.builtOn = builtOn;
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
     * @return the moreInfo
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * @param moreInfo the moreInfo to set
     */
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getKongInfo() {
        return kongInfo;
    }

    public void setKongInfo(String kongInfo) {
        this.kongInfo = kongInfo;
    }

    public String getKongStatus() {
        return kongStatus;
    }

    public void setKongStatus(String kongStatus) {
        this.kongStatus = kongStatus;
    }

    public String getKongCluster() {
        return kongCluster;
    }

    public void setKongCluster(String kongCluster) {
        this.kongCluster = kongCluster;
    }

    public Boolean getMaintenanceModeEnabled() {
        return maintenanceModeEnabled;
    }

    public void setMaintenanceModeEnabled(Boolean maintenanceModeEnabled) {
        this.maintenanceModeEnabled = maintenanceModeEnabled;
    }

    public String getMaintenanceMessage() {
        return maintenanceMessage;
    }

    public void setMaintenanceMessage(String maintenanceMessage) {
        this.maintenanceMessage = maintenanceMessage;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "SystemStatusBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                ", version='" + version + '\'' +
                ", environment='" + environment + '\'' +
                ", builtOn='" + builtOn + '\'' +
                ", kongInfo='" + kongInfo + '\'' +
                ", kongStatus='" + kongStatus + '\'' +
                ", kongCluster='" + kongCluster + '\'' +
                ", maintenanceModeEnabled=" + maintenanceModeEnabled +
                ", maintenanceMessage='" + maintenanceMessage + '\'' +
                ", up=" + up +
                '}';
    }
}
