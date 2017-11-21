package com.t1t.apim.beans.summary;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A single entry in the {@link ApiRegistryBean}.
 */
@XmlRootElement(name = "api")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiEntryBean implements Serializable {

    private static final long serialVersionUID = -7578173174922025902L;

    private String serviceOrgId;
    private String serviceOrgName;
    private String serviceId;
    private String serviceName;
    private String serviceVersion;

    private String planId;
    private String planName;
    private String planVersion;

    private String httpEndpoint;

    private String gatewayId;

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
     * @return the serviceOrgName
     */
    public String getServiceOrgName() {
        return serviceOrgName;
    }

    /**
     * @param serviceOrgName the serviceOrgName to set
     */
    public void setServiceOrgName(String serviceOrgName) {
        this.serviceOrgName = serviceOrgName;
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
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
     * @return the planId
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(String planId) {
        this.planId = planId;
    }

    /**
     * @return the planName
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * @param planName the planName to set
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * @return the planVersion
     */
    public String getPlanVersion() {
        return planVersion;
    }

    /**
     * @param planVersion the planVersion to set
     */
    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    /**
     * @return the httpEndpoint
     */
    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    /**
     * @param httpEndpoint the httpEndpoint to set
     */
    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    /**
     * @return the gatewayId
     */
    public String getGatewayId() {
        return gatewayId;
    }

    /**
     * @param gatewayId the gatewayId to set
     */
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ApiEntryBean [serviceOrgId=" + serviceOrgId + ", serviceOrgName=" + serviceOrgName
                + ", serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceVersion="
                + serviceVersion + ", planId=" + planId + ", planName=" + planName + ", planVersion="
                + planVersion + ", httpEndpoint=" + httpEndpoint + ", gatewayId="
                + gatewayId + "]";
    }

}
