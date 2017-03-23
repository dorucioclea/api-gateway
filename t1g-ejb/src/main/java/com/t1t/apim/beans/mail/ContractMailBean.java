package com.t1t.apim.beans.mail;

/**
 * Created by michallispashidis on 29/04/16.
 */
public class ContractMailBean extends BaseMailBean {
    private String serviceOrgName;
    private String serviceName;
    private String serviceVersion;
    private String planName;
    private String planVersion;
    private String appOrgName;
    private String appName;
    private String appVersion;
    private String userId;
    private String userMail;

    public String getServiceOrgName() {
        return serviceOrgName;
    }

    public void setServiceOrgName(String serviceOrgName) {
        this.serviceOrgName = serviceOrgName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getAppOrgName() {
        return appOrgName;
    }

    public void setAppOrgName(String appOrgName) {
        this.appOrgName = appOrgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanVersion() {
        return planVersion;
    }

    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    @Override
    public String toString() {
        return "ContractRequestMailBean{" +
                "serviceOrgName='" + serviceOrgName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", plan='" + planName + '\'' +
                ", planVersion='" + planVersion + '\'' +
                ", appOrgName='" + appOrgName + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", userId='" + userId + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }
}
