package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;

/**
 * A simple bean showing system status.
 *
 */
public class SystemStatus implements Serializable {
    
    private static final long serialVersionUID = -4419061594392797017L;
    
    private String id;
    private String name;
    private String description;
    private String version;
    private String info;
    private String status;
    private String cluster;
    private boolean up;
    
    /**
     * Constructor.
     */
    public SystemStatus() {
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SystemStatus{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", cluster='" + cluster + '\'' +
                ", up=" + up +
                '}';
    }
}
