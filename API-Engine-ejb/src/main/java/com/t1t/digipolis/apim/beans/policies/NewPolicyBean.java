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
    private String gatewayId;
    private Boolean enabled;

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

    /**
     * @return the gateway id
     */
    public String getGatewayId() {
        return gatewayId;
    }

    /**
     * @param gatewayId the gateway id to set
     */
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    /**
     * @return the enabled value
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

    @Override
    public String toString() {
        return "NewPolicyBean{" +
                "definitionId='" + definitionId + '\'' +
                ", configuration='" + configuration + '\'' +
                ", kongPluginId='" + kongPluginId + '\'' +
                ", contractId=" + contractId +
                ", gatewayId='" + gatewayId + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }
}
