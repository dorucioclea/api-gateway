package com.t1t.digipolis.apim.beans.policies;

import java.io.Serializable;

/**
 * Bean used when creating a new policy for a plan, service, or app.
 *
 */
public class NewPolicyBean implements Serializable {

    private static final long serialVersionUID = -3616888650365376571L;

    private String definitionId;
    private String configuration;

    /**
     * Constructor.
     */
    public NewPolicyBean() {
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
     * @return the definitionId
     */
    public String getDefinitionId() {
        return definitionId;
    }

    /**
     * @param definitionId the definitionId to set
     */
    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewPolicyBean [definitionId=" + definitionId + ", configuration=***]";
    }
}