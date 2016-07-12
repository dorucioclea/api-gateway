package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                return Policies.ANALYTICS;
            case "jwt":
                return Policies.JWT;
            case "acl":
                return Policies.ACL;
            default:
                throw ExceptionFactory.policyDefNotFoundException(pluginName);
        }
    }

}