package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models a Service published to the API Management runtime.
 *
 */
public class Service implements Serializable {

    private static final long serialVersionUID = -294764695917891050L;
    
    private boolean publicService;
    private String organizationId;
    private String serviceId;
    private String version;
    private String endpointType;
    private String endpoint;
    private String basepath;
    private Map<String, String> endpointProperties = new HashMap<>();
    private List<Policy> servicePolicies = new ArrayList<>();
    
    /**
     * Constructor.
     */
    public Service() {
    }

    /**
     * Parametrized constructor
     * @param organizationId
     * @param serviceId
     * @param version
     */
    public Service(String organizationId, String serviceId, String version) {
        this.organizationId = organizationId;
        this.serviceId = serviceId;
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
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the endpointType
     */
    public String getEndpointType() {
        return endpointType;
    }

    /**
     * @param endpointType the endpointType to set
     */
    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the endpointProperties
     */
    public Map<String, String> getEndpointProperties() {
        return endpointProperties;
    }

    /**
     * @param endpointProperties the endpointProperties to set
     */
    public void setEndpointProperties(Map<String, String> endpointProperties) {
        this.endpointProperties = endpointProperties;
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
     * @return the publicService
     */
    public boolean isPublicService() {
        return publicService;
    }

    /**
     * @param publicService the publicService to set
     */
    public void setPublicService(boolean publicService) {
        this.publicService = publicService;
    }

    /**
     * @return the servicePolicies
     */
    public List<Policy> getServicePolicies() {
        return servicePolicies;
    }

    /**
     * @param servicePolicies the servicePolicies to set
     */
    public void setServicePolicies(List<Policy> servicePolicies) {
        this.servicePolicies = servicePolicies;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
        result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
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
        Service other = (Service) obj;
        if (organizationId == null) {
            if (other.organizationId != null)
                return false;
        } else if (!organizationId.equals(other.organizationId))
            return false;
        if (serviceId == null) {
            if (other.serviceId != null)
                return false;
        } else if (!serviceId.equals(other.serviceId))
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
        return "Service{" +
                "publicService=" + publicService +
                ", organizationId='" + organizationId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", version='" + version + '\'' +
                ", endpointType='" + endpointType + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", basepath='" + basepath + '\'' +
                ", endpointProperties=" + endpointProperties +
                ", servicePolicies=" + servicePolicies +
                '}';
    }
}
