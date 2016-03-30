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
    private String kongPluginId;
    private Long contractId;

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

    /**
     *
     * @return the kongPluginId
     */
    public String getKongPluginId() {
        return kongPluginId;
    }

    /**
     *
     * @param kongPluginId the kongPluginId to set
     */
    public void setKongPluginId(String kongPluginId) {
        this.kongPluginId = kongPluginId;
    }

    /**
     * @return the contractId
     */
    public Long getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "NewPolicyBean [definitionId=" + definitionId + ", configuration=***, kongPluginId=" + kongPluginId + "]";
    }
}
