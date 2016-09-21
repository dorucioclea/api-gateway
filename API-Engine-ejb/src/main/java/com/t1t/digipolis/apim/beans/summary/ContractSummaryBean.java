package com.t1t.digipolis.apim.beans.summary;

import com.t1t.digipolis.apim.beans.contracts.ContractBean;

import java.io.Serializable;
import java.util.Date;

/**
 * A summary bean for a contract.  Includes information useful for displaying
 * the contract in a list in a UI.
 *
 */
public class ContractSummaryBean implements Serializable {

    private static final long serialVersionUID = 1412354024017539782L;

    private Long contractId;
    private String appOrganizationId;
    private String appOrganizationName;
    private String appId;
    private String appName;
    private String appVersion;
    private String serviceOrganizationId;
    private String serviceOrganizationName;
    private String serviceId;
    private String serviceName;
    private String serviceVersion;
    private String serviceDescription;
    private String planName;
    private String planId;
    private String planVersion;
    private String provisionKey;
    private String apikey;
    private Date createdOn;
    private Boolean termsAgreed;

    /**
     * Constructor.
     */
    public ContractSummaryBean() {
    }

    public ContractSummaryBean(ContractBean c) {
        this.contractId = c.getId();
        this.appOrganizationId = c.getApplication().getApplication().getOrganization().getId();
        this.appOrganizationName = c.getApplication().getApplication().getOrganization().getName();
        this.appId = c.getApplication().getApplication().getId();
        this.appName = c.getApplication().getApplication().getName();
        this.apikey = c.getApplication().getApikey();
        this.appVersion = c.getApplication().getVersion();
        this.serviceOrganizationId = c.getService().getService().getOrganization().getId();
        this.serviceOrganizationName = c.getService().getService().getOrganization().getName();
        this.serviceId = c.getService().getService().getId();
        this.serviceName = c.getService().getService().getName();
        this.serviceVersion = c.getService().getVersion();
        this.serviceDescription = c.getService().getService().getDescription();
        this.planName = c.getPlan().getPlan().getName();
        this.planId = c.getPlan().getPlan().getId();
        this.planVersion = c.getPlan().getVersion();
        this.provisionKey = c.getService().getProvisionKey();
        this.createdOn = c.getCreatedOn();
        this.termsAgreed = c.getTermsAgreed();
    }

    /**
     * @return the appOrganizationId
     */
    public String getAppOrganizationId() {
        return appOrganizationId;
    }

    /**
     * @param appOrganizationId the appOrganizationId to set
     */
    public void setAppOrganizationId(String appOrganizationId) {
        this.appOrganizationId = appOrganizationId;
    }

    /**
     * @return the appOrganizationName
     */
    public String getAppOrganizationName() {
        return appOrganizationName;
    }

    /**
     * @param appOrganizationName the appOrganizationName to set
     */
    public void setAppOrganizationName(String appOrganizationName) {
        this.appOrganizationName = appOrganizationName;
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion the appVersion to set
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return the serviceOrganizationId
     */
    public String getServiceOrganizationId() {
        return serviceOrganizationId;
    }

    /**
     * @param serviceOrganizationId the serviceOrganizationId to set
     */
    public void setServiceOrganizationId(String serviceOrganizationId) {
        this.serviceOrganizationId = serviceOrganizationId;
    }

    /**
     * @return the serviceOrganizationName
     */
    public String getServiceOrganizationName() {
        return serviceOrganizationName;
    }

    /**
     * @param serviceOrganizationName the serviceOrganizationName to set
     */
    public void setServiceOrganizationName(String serviceOrganizationName) {
        this.serviceOrganizationName = serviceOrganizationName;
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
     * @return the serviceDescription
     */
    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * @param serviceDescription the serviceDescription to set
     */
    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
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
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
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
     * @return the provision key
     */
    public String getProvisionKey() {
        return provisionKey;
    }

    /**
     * @param provisionKey the provision key to set
     */
    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    /**
     * @return the value of termsAgreed
     */
    public Boolean getTermsAgreed() {
        return termsAgreed;
    }

    /**
     * @param termsAgreed the value to set
     */
    public void setTermsAgreed(Boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    /**
     * @return the apikey
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * @param apikey the apikey to set
     */
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }


    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
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
        ContractSummaryBean other = (ContractSummaryBean) obj;
        if (contractId == null) {
            if (other.contractId != null)
                return false;
        } else if (!contractId.equals(other.contractId))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ContractSummaryBean [contractId=" + contractId + ", apikey=" + apikey
                + ", appOrganizationId=" + appOrganizationId + ", appOrganizationName=" + appOrganizationName
                + ", appId=" + appId + ", appName=" + appName + ", appVersion=" + appVersion
                + ", serviceOrganizationId=" + serviceOrganizationId + ", serviceOrganizationName="
                + serviceOrganizationName + ", serviceId=" + serviceId + ", serviceName=" + serviceName
                + ", serviceVersion=" + serviceVersion + ", serviceDescription=" + serviceDescription
                + ", planName=" + planName + ", planId=" + planId + ", planVersion=" + planVersion
                + ", createdOn=" + createdOn + "]";
    }

}
