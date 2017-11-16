package com.t1t.apim.beans.authorization;

import java.io.Serializable;

/**
 * Created by michallispashidis on 9/09/15.
 */
public abstract class AuthConsumerRequestBean implements Serializable {
    private String consumerUniqueNameOrId;
    private String orgId;
    private String appId;
    private String appVersion;

    public AuthConsumerRequestBean(String consumerUniqueNameOrId, String orgId, String appId, String appVersion) {
        this.consumerUniqueNameOrId = consumerUniqueNameOrId;
        this.orgId = orgId;
        this.appId = appId;
        this.appVersion = appVersion;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getConsumerUniqueNameOrId() {
        return consumerUniqueNameOrId;
    }

    public void setConsumerUniqueNameOrId(String consumerUniqueNameOrId) {
        this.consumerUniqueNameOrId = consumerUniqueNameOrId;
    }

    @Override
    public String toString() {
        return "AuthConsumerRequestBean{" +
                "consumerUniqueNameOrId='" + consumerUniqueNameOrId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}
