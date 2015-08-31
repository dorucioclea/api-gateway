package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.kong.model.*;

/**
 * Created by michallispashidis on 31/08/15.
 */
public enum Policies {
    //TODO we can create a plugin framework for policies, but here it's still hard coded, but not a bad start :-)
    BASICAUTHENTICATION(KongPluginBasicAuth.class)
    , CORS(KongPluginCors.class)
    , FILELOG(KongPluginFileLog.class)
    , HTTPLOG(KongPluginHttpLog.class)
    , UDPLOG(KongPluginUdpLog.class)
    , TCPLOG(KongPluginTcpLog.class)
    , IPRESTRICTION(KongPluginIPRestriction.class)
    , KEYAUTHENTICATION(KongPluginKeyAuth.class)
    , OAUTH2(KongPluginOAuth.class)
    , RATELIMITING(KongPluginRateLimiting.class)
    , REQUESTSIZELIMITING(KongPluginRequestSizeLimiting.class)
    , REQUESTTRANSFORMER(KongPluginRequestTransformer.class)
    , RESPONSETRANSFORMER(KongPluginResponseTransformer.class)
    , SSL(KongPluginSSL.class);

    private Class clazz;

    Policies(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return "PolicyConstants{" +
                ", clazz=" + clazz +
                '}';
    }

}
