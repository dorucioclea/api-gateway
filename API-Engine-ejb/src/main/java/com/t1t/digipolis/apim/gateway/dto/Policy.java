package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;

/**
 * Models a policy.
 *
 */
public class Policy implements Serializable {

    private static final long serialVersionUID = -5945877012261045491L;
    
    private String policyJsonConfig; //config_info json str
    private String policyImpl; //Reference to policy def id
    private String kongPluginId;
    private String entityId;
    private Long policyId;

    /**
     * Constructor.
     */
    public Policy() {
    }

    public Policy(String policyImpl, String policyJsonConfig, String entityId) {
        this.policyJsonConfig = policyJsonConfig;
        this.policyImpl = policyImpl;
        this.entityId = entityId;
    }

    /**
     * @return the policyClass
     */
    public String getPolicyImpl() {
        return policyImpl;
    }

    /**
     * @param policyClass the policyClass to set
     */
    public void setPolicyImpl(String policyClass) {
        this.policyImpl = policyClass;
    }

    /**
     * @return the policyJsonConfig
     */
    public String getPolicyJsonConfig() {
        return policyJsonConfig;
    }

    /**
     * @param policyJsonConfig the policyJsonConfig to set
     */
    public void setPolicyJsonConfig(String policyJsonConfig) {
        this.policyJsonConfig = policyJsonConfig;
    }

    /**
     * @return the policy id
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * @param policyId the policy id to set
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * @return the Kong plugin id
     */
    public String getKongPluginId() {
        return kongPluginId;
    }

    /**
     * @param kongPluginId the Kong plugin id to set
     */
    public void setKongPluginId(String kongPluginId) {
        this.kongPluginId = kongPluginId;
    }

    /**
     * @return the entity id
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entity id to set
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Policy policy = (Policy) o;

        if (policyJsonConfig != null ? !policyJsonConfig.equals(policy.policyJsonConfig) : policy.policyJsonConfig != null)
            return false;
        if (policyImpl != null ? !policyImpl.equals(policy.policyImpl) : policy.policyImpl != null) return false;
        if (kongPluginId != null ? !kongPluginId.equals(policy.kongPluginId) : policy.kongPluginId != null)
            return false;
        return policyId != null ? policyId.equals(policy.policyId) : policy.policyId == null;

    }

    @Override
    public int hashCode() {
        int result = policyJsonConfig != null ? policyJsonConfig.hashCode() : 0;
        result = 31 * result + (policyImpl != null ? policyImpl.hashCode() : 0);
        result = 31 * result + (kongPluginId != null ? kongPluginId.hashCode() : 0);
        result = 31 * result + (policyId != null ? policyId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "policyJsonConfig='" + policyJsonConfig + '\'' +
                ", policyImpl='" + policyImpl + '\'' +
                ", kongPluginId='" + kongPluginId + '\'' +
                ", entityId='" + entityId + '\'' +
                ", policyId=" + policyId +
                '}';
    }
}
