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
    BASICAUTHENTICATION(KongPluginBasicAuth.class,"basic-auth", "BasicAuthentication")
    , CORS(KongPluginCors.class,"cors", "CORS")
    , FILELOG(KongPluginFileLog.class,"file-log", "FileLog")
    , HTTPLOG(KongPluginHttpLog.class,"http-log", "HTTPLog")
    , UDPLOG(KongPluginUdpLog.class,"udp-log", "UDPLog")
    , TCPLOG(KongPluginTcpLog.class,"tcp-log", "TCPLog")
    , IPRESTRICTION(KongPluginIPRestriction.class,"ip-restriction", "IPRestriction")
    , KEYAUTHENTICATION(KongPluginKeyAuth.class,"key-auth", "KeyAuthentication")
    , OAUTH2(KongPluginOAuth.class,"oauth2", "OAuth2")
    , RATELIMITING(KongPluginRateLimiting.class,"rate-limiting", "RateLimiting")
    , REQUESTSIZELIMITING(KongPluginRequestSizeLimiting.class,"request-size-limiting", "RequestSizeLimiting")
    , REQUESTTRANSFORMER(KongPluginRequestTransformer.class,"request-transformer", "RequestTransformer")
    , RESPONSETRANSFORMER(KongPluginResponseTransformer.class,"response-transformer", "ResponseTransformer")
    , SSL(KongPluginSSL.class,"ssl", "SSL")
    , ANALYTICS(KongPluginAnalytics.class,"mashape-analytics", "Analytics")
    , JWT(KongPluginJWT.class,"jwt", "JWT")
    , ACL(KongPluginACL.class,"acl", "ACL");

    private Class clazz;
    private String kongIdentifier;
    private String policyDefId;

    Policies(Class clazz, String identifier, String policyDefId) {
        this.clazz = clazz;
        this.kongIdentifier = identifier;
        this.policyDefId = policyDefId;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getKongIdentifier() {
        return kongIdentifier;
    }

    public String getPolicyDefId() {
        return this.policyDefId;
    }

    @Override
    public String toString() {
        return "PolicyConstants{" +
                ", clazz=" + clazz +
                '}';
    }

}
