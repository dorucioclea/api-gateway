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
    private String contractApiKey;
    private String customId; //kong -> custom id
    //private String userId; //generated:: org.app.version.customid

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

    public String getContractApiKey() {
        return contractApiKey;
    }

    public void setContractApiKey(String contractApiKey) {
        this.contractApiKey = contractApiKey;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return "AbstractAuthConsumerRequest{" +
                "orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", contractApiKey='" + contractApiKey + '\'' +
                ", customId='" + customId + '\'' +
                '}';
    }
}
