package com.t1t.apim.beans.audit.data;

import com.t1t.apim.beans.contracts.ContractBean;

import java.io.Serializable;

/**
 * The data saved along with the audit entry when a contract is created.
 */
public class ContractData implements Serializable {

    private static final long serialVersionUID = -937575521565548994L;

    private String appOrgId;
    private String appId;
    private String appVersion;
    private String serviceOrgId;
    private String serviceId;
    private String serviceVersion;
    private String planId;
    private String planVersion;

    /**
     * Constructor.
     */
    public ContractData() {
    }

    /**
     * Constructor.
     * @param bean the contract
     */
    public ContractData(ContractBean bean) {
        setAppOrgId(bean.getApplication().getApplication().getOrganization().getId());
        setAppId(bean.getApplication().getApplication().getId());
        setAppVersion(bean.getApplication().getVersion());
        setServiceOrgId(bean.getService().getService().getOrganization().getId());
        setServiceId(bean.getService().getService().getId());
        setServiceVersion(bean.getService().getVersion());
        setPlanId(bean.getPlan().getPlan().getId());
        setPlanVersion(bean.getPlan().getVersion());
    }

    /**
     * @return the appOrgId
     */
    public String getAppOrgId() {
        return appOrgId;
    }

    /**
     * @param appOrgId the appOrgId to set
     */
    public void setAppOrgId(String appOrgId) {
        this.appOrgId = appOrgId;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ContractData [appOrgId=" + appOrgId + ", appId=" + appId + ", appVersion=" + appVersion
                + ", serviceOrgId=" + serviceOrgId + ", serviceId=" + serviceId + ", serviceVersion="
                + serviceVersion + ", planId=" + planId + ", planVersion=" + planVersion + "]";
    }

}
