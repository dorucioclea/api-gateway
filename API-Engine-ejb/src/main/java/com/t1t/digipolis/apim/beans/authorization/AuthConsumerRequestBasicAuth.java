package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestBasicAuth extends AuthConsumerRequestBean {
    private String password;
    public AuthConsumerRequestBasicAuth(String consumerUniqueNameOrId, String orgId, String appId, String appVersion) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
    }

    public AuthConsumerRequestBasicAuth(String consumerUniqueNameOrId, String orgId, String appId, String appVersion, String password) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
        this.password = password;
    }
}
