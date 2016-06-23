package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Models an Application registered with the API Management runtime.
 *
 */
public class Application implements Serializable {
    
    private static final long serialVersionUID = 4515000941548789924L;
    private String applicationName;
    private String organizationId;
    private String applicationId;
    private String version;
    private Set<Contract> contracts = new HashSet<>();
    
    /**
     * Constructor.
     */
    public Application() {
    }

    public Application(String organizationId, String applicationId, String version) {
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.version = version;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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
     * @return the contracts
     */
    public Set<Contract> getContracts() {
        return contracts;
    }
    
    /**
     * @param contract the contract to add
     */
    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    /**
     * @param contracts the contracts to set
     */
    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
        result = prime * result + ((getApplicationId() == null) ? 0 : getApplicationId().hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        Application other = (Application) obj;
        if (organizationId == null) {
            if (other.organizationId != null)
                return false;
        } else if (!organizationId.equals(other.organizationId))
            return false;
        if (getApplicationId() == null) {
            if (other.getApplicationId() != null)
                return false;
        } else if (!getApplicationId().equals(other.getApplicationId()))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationName='" + applicationName + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", version='" + version + '\'' +
                ", contracts=" + contracts +
                '}';
    }
}
