package com.t1t.digipolis.apim.beans.events;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ContractRequest extends Event {
    private String serviceOrg;
    private String serviceId;
    private String serviceVersion;
    private String appOrg;
    private String appId;
    private String appVersion;

    public ContractRequest(EventBean bean) {
        super(bean);
    }

    public String getServiceOrg() {
        return serviceOrg;
    }

    public void setServiceOrg(String serviceOrg) {
        this.serviceOrg = serviceOrg;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getAppOrg() {
        return appOrg;
    }

    public void setAppOrg(String appOrg) {
        this.appOrg = appOrg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "ContractRequest{" +
                "serviceOrg='" + serviceOrg + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", appOrg='" + appOrg + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}