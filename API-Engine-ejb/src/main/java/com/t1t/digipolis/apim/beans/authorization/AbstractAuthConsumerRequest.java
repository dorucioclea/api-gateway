package com.t1t.digipolis.apim.beans.authorization;

import java.io.Serializable;

/**
 * Created by michallispashidis on 16/09/15.
 */
public abstract class AbstractAuthConsumerRequest implements Serializable {
    //set the context
    private String orgId;
    private String appId;
    private String appVersion;
    private String appApiKey;
    private String customId; //kong -> custom id
    private String userId; //generated:: org.app.version.customid

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

    public String getAppApiKey() {
        return appApiKey;
    }

    public void setAppApiKey(String appApiKey) {
        this.appApiKey = appApiKey;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AbstractAuthConsumerRequest{" +
                "orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appApiKey='" + appApiKey + '\'' +
                ", customId='" + customId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
