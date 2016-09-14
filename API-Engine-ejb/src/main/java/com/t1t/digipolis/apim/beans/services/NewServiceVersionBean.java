package com.t1t.digipolis.apim.beans.services;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when creating a new version of a service.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewServiceVersionBean implements Serializable {

    private static final long serialVersionUID = 7207058698209555294L;

    private String version;
    private boolean clone;
    private String cloneVersion;

    /**
     * Constructor.
     */
    public NewServiceVersionBean() {
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
     * @return the clone
     */
    public boolean isClone() {
        return clone;
    }

    /**
     * @param clone the clone to set
     */
    public void setClone(boolean clone) {
        this.clone = clone;
    }

    /**
     * @return the cloneVersion
     */
    public String getCloneVersion() {
        return cloneVersion;
    }

    /**
     * @param cloneVersion the cloneVersion to set
     */
    public void setCloneVersion(String cloneVersion) {
        this.cloneVersion = cloneVersion;
    }

    @Override
    public String toString() {
        return "NewServiceVersionBean{" +
                "version='" + version + '\'' +
                ", clone=" + clone +
                ", cloneVersion='" + cloneVersion + '\'' +
                '}';
    }
}
