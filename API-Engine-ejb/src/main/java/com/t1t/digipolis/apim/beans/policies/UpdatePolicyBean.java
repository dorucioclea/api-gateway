package com.t1t.digipolis.apim.beans.policies;

import java.io.Serializable;

/**
 * Bean used when updating a policy.
 *
 */
public class UpdatePolicyBean implements Serializable {

    private static final long serialVersionUID = 1625082587625332040L;

    private String configuration;

    /**
     * Constructor.
     */
    public UpdatePolicyBean() {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdatePolicyBean [configuration=***]";
    }

}
