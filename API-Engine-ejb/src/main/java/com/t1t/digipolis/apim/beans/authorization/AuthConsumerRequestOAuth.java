package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestOAuth extends AuthConsumerRequestBean{
    //aplication name we have already but can be overriden
    String appName;
    String clientId;
    String clientSecred;
    String redirectURI;

    public AuthConsumerRequestOAuth(String consumerUniqueNameOrId, String orgId, String appId, String appVersion, String appName, String clientId, String clientSecred, String redirectURI) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
        this.appName = appName;
        this.clientId = clientId;
        this.clientSecred = clientSecred;
        this.redirectURI = redirectURI;
    }

    public AuthConsumerRequestOAuth(String consumerUniqueNameOrId, String orgId, String appId, String appVersion) {
        super(consumerUniqueNameOrId, orgId, appId, appVersion);
    }
}
