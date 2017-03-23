package com.t1t.apim.beans.apps;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Bean used when creating a new application version.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewApplicationVersionBean implements Serializable {

    private static final long serialVersionUID = 960818800225855945L;

    private String version;
    private boolean clone;
    private String cloneVersion;

    /**
     * Constructor.
     */
    public NewApplicationVersionBean() {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewApplicationVersionBean [version=" + version + ", clone=" + clone + ", cloneVersion="
                + cloneVersion + "]";
    }

}
