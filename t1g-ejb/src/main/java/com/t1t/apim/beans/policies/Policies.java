package com.t1t.apim.beans.policies;

import com.t1t.kong.model.*;

/**
 * Created by michallispashidis on 31/08/15.
 * !Important the string name in the constructor must be the kong identifier
 */
public enum Policies {
    //TODO we can create a plugin framework for policies, but here it's still hard coded, but not a bad start :-)
    // the kong identifier must be the official plugin identifier, the policyDefId must be the db policydef id
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
    , ANALYTICS(KongPluginAnalytics.class,"galileo", "Galileo")
    , JWT(KongPluginJWT.class,"jwt", "JWT")
    , JWTUP(KongPluginJWTUp.class,"jwt-up", "JWTUp")
    , ACL(KongPluginACL.class,"acl", "ACL")
    , LDAPAUTHENTICATION(KongPluginLDAP.class, "ldap-auth", "LDAPAuthentication")
    , JSONTHREATPROTECTION(KongPluginJsonThreatProtection.class,"json-threat-protection","JSONThreatProtection")
    , HAL(KongPluginEmptyConfig.class, "hal", "HAL")
    , DATADOG(KongPluginDataDog.class, "datadog", "DataDog");

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
