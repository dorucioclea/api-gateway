package com.t1t.apim.beans.authorization;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class OAuthConsumerRequestBean {
    private String appOAuthId;
    private String appOAuthSecret;
    private String uniqueUserName;

    public OAuthConsumerRequestBean() {
    }

    public String getAppOAuthId() {
        return appOAuthId;
    }

    public void setAppOAuthId(String appOAuthId) {
        this.appOAuthId = appOAuthId;
    }

    public String getAppOAuthSecret() {
        return appOAuthSecret;
    }

    public void setAppOAuthSecret(String appOAuthSecret) {
        this.appOAuthSecret = appOAuthSecret;
    }

    public String getUniqueUserName() {
        return uniqueUserName;
    }

    public void setUniqueUserName(String uniqueUserName) {
        this.uniqueUserName = uniqueUserName;
    }

    @Override
    public String toString() {
        return "OAuthConsumerRequestBean{" +
                "appOAuthId='" + appOAuthId + '\'' +
                ", appOAuthSecret='" + appOAuthSecret + '\'' +
                ", uniqueUserName='" + uniqueUserName + '\'' +
                '}';
    }
}
