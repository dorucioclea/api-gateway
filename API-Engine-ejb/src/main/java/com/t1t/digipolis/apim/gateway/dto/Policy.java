package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;

/**
 * Models a policy.
 *
 */
public class Policy implements Serializable {

    private static final long serialVersionUID = -5945877012261045491L;
    
    private String policyJsonConfig; //config_info json str
    private String policyImpl; //Reference to policy (classname?) we're going to load?

    /**
     * Constructor.
     */
    public Policy() {
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
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((policyImpl == null) ? 0 : policyImpl.hashCode());
        result = prime * result + ((policyJsonConfig == null) ? 0 : policyJsonConfig.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Policy other = (Policy) obj;
        if (policyImpl == null) {
            if (other.policyImpl != null)
                return false;
        } else if (!policyImpl.equals(other.policyImpl))
            return false;
        if (policyJsonConfig == null) {
            if (other.policyJsonConfig != null)
                return false;
        } else if (!policyJsonConfig.equals(other.policyJsonConfig))
            return false;
        return true;
    }
}
