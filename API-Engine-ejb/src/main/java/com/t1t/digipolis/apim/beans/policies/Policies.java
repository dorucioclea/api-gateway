package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.kong.model.KongPluginAnalytics;
import com.t1t.digipolis.kong.model.KongPluginBasicAuth;
import com.t1t.digipolis.kong.model.KongPluginCors;
import com.t1t.digipolis.kong.model.KongPluginFileLog;
import com.t1t.digipolis.kong.model.KongPluginHttpLog;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginRateLimiting;
import com.t1t.digipolis.kong.model.KongPluginRequestSizeLimiting;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformer;
import com.t1t.digipolis.kong.model.KongPluginSSL;
import com.t1t.digipolis.kong.model.KongPluginTcpLog;
import com.t1t.digipolis.kong.model.KongPluginUdpLog;

/**
 * Created by michallispashidis on 31/08/15.
 * !Important the string name in the constructor must be the kong identifier
 */
public enum Policies {
    //TODO we can create a plugin framework for policies, but here it's still hard coded, but not a bad start :-)
    BASICAUTHENTICATION(KongPluginBasicAuth.class,"basic-auth")
    , CORS(KongPluginCors.class,"cors")
    , FILELOG(KongPluginFileLog.class,"file-log")
    , HTTPLOG(KongPluginHttpLog.class,"http-log")
    , UDPLOG(KongPluginUdpLog.class,"udp-log")
    , TCPLOG(KongPluginTcpLog.class,"tcp-log")
    , IPRESTRICTION(KongPluginIPRestriction.class,"ip-restriction")
    , KEYAUTHENTICATION(KongPluginKeyAuth.class,"key-auth")
    , OAUTH2(KongPluginOAuth.class,"oauth2")
    , RATELIMITING(KongPluginRateLimiting.class,"rate-limiting")
    , REQUESTSIZELIMITING(KongPluginRequestSizeLimiting.class,"request-size-limiting")
    , REQUESTTRANSFORMER(KongPluginRequestTransformer.class,"request-transformer")
    , RESPONSETRANSFORMER(KongPluginResponseTransformer.class,"response-transformer")
    , SSL(KongPluginSSL.class,"ssl")
    , ANALYTICS(KongPluginAnalytics.class,"mashape-analytics");

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
