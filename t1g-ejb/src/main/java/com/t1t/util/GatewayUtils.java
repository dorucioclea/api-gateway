package com.t1t.util;

import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.exceptions.ExceptionFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class GatewayUtils {

    public static final Policies convertKongPluginNameToPolicy(String pluginName) {
        switch (pluginName) {
            case "basic-auth":
                return Policies.BASICAUTHENTICATION;
            case "cors":
                return Policies.CORS;
            case "file-log":
                return Policies.FILELOG;
            case "http-log":
                return Policies.HTTPLOG;
            case "udp-log":
                return Policies.UDPLOG;
            case "tcp-log":
                return Policies.TCPLOG;
            case "ip-restriction":
                return Policies.IPRESTRICTION;
            case "key-auth":
                return Policies.KEYAUTHENTICATION;
            case "oauth2":
                return Policies.OAUTH2;
            case "rate-limiting":
                return Policies.RATELIMITING;
            case "request-size-limiting":
                return Policies.REQUESTSIZELIMITING;
            case "request-transformer":
                return Policies.REQUESTTRANSFORMER;
            case "response-transformer":
                return Policies.RESPONSETRANSFORMER;
            case "ssl":
                return Policies.SSL;
            case "mashape-analytics":
            case "galileo":
                return Policies.ANALYTICS;
            case "jwt":
                return Policies.JWT;
            case "acl":
                return Policies.ACL;
            case "hal":
                return Policies.HAL;
            case "jwt-up":
                return Policies.JWTUP;
            case "ldap-auth":
                return Policies.LDAPAUTHENTICATION;
            case "json-threat-protection":
                return Policies.JSONTHREATPROTECTION;
            default:
                throw ExceptionFactory.policyDefNotFoundException(pluginName);
        }
    }

}