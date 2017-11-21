package com.t1t.apim.beans.policies;

import java.io.Serializable;

/**
 * Bean used when updating a policy.
 */
public class UpdatePolicyBean implements Serializable {

    private static final long serialVersionUID = 1625082587625332040L;

    private String configuration;
    private Boolean enabled;

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

    /**
     * @return the value of enabled
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "UpdatePolicyBean{" +
                "configuration='" + configuration + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
