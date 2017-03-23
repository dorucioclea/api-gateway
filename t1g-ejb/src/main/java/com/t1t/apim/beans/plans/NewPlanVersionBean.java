package com.t1t.apim.beans.plans;

import java.io.Serializable;

/**
 * Bean used when creating a new version of a plan.
 *
 */
public class NewPlanVersionBean implements Serializable {

    private static final long serialVersionUID = 1828038441268775749L;

    private String version;
    private boolean clone;
    private String cloneVersion;

    /**
     * Constructor.
     */
    public NewPlanVersionBean() {
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
        return "NewPlanVersionBean [version=" + version + ", clone=" + clone + ", cloneVersion="
                + cloneVersion + "]";
    }

}
