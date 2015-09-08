package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.kong.model.*;

/**
 * Created by michallispashidis on 31/08/15.
 */
public enum Policies {
    //TODO we can create a plugin framework for policies, but here it's still hard coded, but not a bad start :-)
    BASICAUTHENTICATION(KongPluginBasicAuth.class,"basicauth")
    , CORS(KongPluginCors.class,"cors")
    , FILELOG(KongPluginFileLog.class,"filelog")
    , HTTPLOG(KongPluginHttpLog.class,"httplog")
    , UDPLOG(KongPluginUdpLog.class,"udplog")
    , TCPLOG(KongPluginTcpLog.class,"tcplog")
    , IPRESTRICTION(KongPluginIPRestriction.class,"ip_restriction")
    , KEYAUTHENTICATION(KongPluginKeyAuth.class,"keyauth")
    , OAUTH2(KongPluginOAuth.class,"oauth2")
    , RATELIMITING(KongPluginRateLimiting.class,"ratelimiting")
    , REQUESTSIZELIMITING(KongPluginRequestSizeLimiting.class,"requestsizelimiting")
    , REQUESTTRANSFORMER(KongPluginRequestTransformer.class,"request_transformer")
    , RESPONSETRANSFORMER(KongPluginResponseTransformer.class,"response_transformer")
    , SSL(KongPluginSSL.class,"ssl");

    private Class clazz;
    private String kongIdentifier;

    Policies(Class clazz, String identifier) {
        this.clazz = clazz;
        this.kongIdentifier = identifier;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getKongIdentifier() {
        return kongIdentifier;
    }

    @Override
    public String toString() {
        return "PolicyConstants{" +
                ", clazz=" + clazz +
                '}';
    }

}