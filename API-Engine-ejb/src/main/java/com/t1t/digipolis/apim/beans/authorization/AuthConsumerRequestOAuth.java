package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestOAuth extends AbstractAuthConsumerRequest{
    //aplication name we have already but can be overriden
    String appName;
    String clientId;
    String clientSecred;
    String redirectURI;

}
