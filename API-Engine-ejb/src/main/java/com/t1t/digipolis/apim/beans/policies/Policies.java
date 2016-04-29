package com.t1t.digipolis.apim.beans.policies;

import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongApiList;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongInstalledPlugins;
import com.t1t.digipolis.kong.model.KongOAuthTokenList;
import com.t1t.digipolis.kong.model.KongPluginBasicAuth;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.kong.model.KongPluginJWTRequest;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.kong.model.KongPluginACLRequest;
import com.t1t.digipolis.kong.model.KongPluginACL;
import com.t1t.digipolis.kong.model.KongPluginACLResponseList;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongStatus;
import com.t1t.digipolis.kong.model.*;

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
    , ANALYTICS(KongPluginAnalytics.class,"mashape-analytics")
    , JWT(KongPluginJWT.class,"jwt")
    , ACL(KongPluginACL.class,"acl");

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
