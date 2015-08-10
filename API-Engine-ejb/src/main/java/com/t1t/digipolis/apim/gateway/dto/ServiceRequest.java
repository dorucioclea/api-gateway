package com.t1t.digipolis.apim.gateway.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An inbound request for a managed service.
 *
 */
public class ServiceRequest implements IServiceObject, Serializable {
    
    private static final long serialVersionUID = 8024669261165845962L;

    private String apiKey;
    private transient ServiceContract contract;
    private String type;
    private String destination;
    private Map<String, String> queryParams = new LinkedHashMap<>();
    private Map<String, String> headers = new HeaderHashMap();
    private String remoteAddr;
    private Object rawRequest;
    private boolean transportSecurity = false;
    
    /*
     * Optional fields - set these if you want the APIMan engine to
     * validate that the apikey is valid for the given service coords.
     */
    private String serviceOrgId;
    private String serviceId;
    private String serviceVersion;

    /**
     * Constructor.
     */
    public ServiceRequest() {
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the rawRequest
     */
    public Object getRawRequest() {
        return rawRequest;
    }

    /**
     * @param rawRequest the rawRequest to set
     */
    public void setRawRequest(Object rawRequest) {
        this.rawRequest = rawRequest;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
   
    /**
     * @see io.apiman.gateway.engine.beans.IServiceObject#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    /**
     * @see io.apiman.gateway.engine.beans.IServiceObject#setHeaders(Map)
     */
    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the remoteAddr
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * @param remoteAddr the remoteAddr to set
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * @return the contract
     */
    public ServiceContract getContract() {
        return contract;
    }

    /**
     * @param contract the contract to set
     */
    public void setContract(ServiceContract contract) {
        this.contract = contract;
    }

    /**
     * @return the serviceOrgId
     */
    public String getServiceOrgId() {
        return serviceOrgId;
    }

    /**
     * @param serviceOrgId the serviceOrgId to set
     */
    public void setServiceOrgId(String serviceOrgId) {
        this.serviceOrgId = serviceOrgId;
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
     * @return the serviceVersion
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * @return the queryParams
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * @param queryParams the queryParams to set
     */
    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * Indicates whether service request or response was made with transport security.
     *
     * @return true if transport is secure; else false.
     */
    public boolean isTransportSecure() {
        return transportSecurity;
    }

    /**
     * Set whether service request/response was made with transport security.
     * 
     * @param isSecure transport security status
     */
    public void setTransportSecure(boolean isSecure) {
        this.transportSecurity = isSecure;
    }
}
