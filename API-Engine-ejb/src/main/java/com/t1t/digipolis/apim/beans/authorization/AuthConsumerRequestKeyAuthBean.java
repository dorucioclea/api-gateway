package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestKeyAuthBean extends AuthConsumerRequestBean {
    private String key;
    public AuthConsumerRequestKeyAuthBean(String consumerUniqueNameOrId, String orgId, String appId, String appVersion) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
        this.key = null;
    }

    public AuthConsumerRequestKeyAuthBean(String consumerUniqueNameOrId, String orgId, String appId, String appVersion, String key) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
        this.key = key;
    }
}
