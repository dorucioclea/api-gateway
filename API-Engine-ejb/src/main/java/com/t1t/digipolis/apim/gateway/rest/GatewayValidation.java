package com.t1t.digipolis.apim.gateway.rest;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuthEnhanced;
import com.t1t.digipolis.kong.model.KongPluginOAuthScope;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michallispashidis on 30/09/15.
 */
public class GatewayValidation {

    /**
     * Validate OAuth plugin values and if necessary transform.
     *
     * @param policy    OAuth policy
     * @return
     */
    public static synchronized Policy validateOAuth(Policy policy) {
        //we can be sure this is an OAuth Policy
        Gson gson = new Gson();
        KongPluginOAuth oauthValue = gson.fromJson(policy.getPolicyJsonConfig(), KongPluginOAuth.class);
        KongPluginOAuthEnhanced newOAuthValue = new KongPluginOAuthEnhanced();
        newOAuthValue.setEnableImplicitGrant(oauthValue.getEnableImplicitGrant());
        newOAuthValue.setEnableAuthorizationCode(oauthValue.getEnableAuthorizationCode());
        newOAuthValue.setEnableClientCredentials(oauthValue.getEnableClientCredentials());
        newOAuthValue.setEnablePasswordGrant(oauthValue.getEnablePasswordGrant());
        newOAuthValue.setHideCredentials(oauthValue.getHideCredentials());
        newOAuthValue.setMandatoryScope(oauthValue.getMandatoryScope());
        newOAuthValue.setProvisionKey(oauthValue.getProvisionKey());
        newOAuthValue.setTokenExpiration(oauthValue.getTokenExpiration());
        List<KongPluginOAuthScope> scopeObjects = oauthValue.getScopes();
        List<Object>scopes = new ArrayList<>();
        for(KongPluginOAuthScope scope:scopeObjects){
            scopes.add(scope.getScope());
        }
        newOAuthValue.setScopes(scopes);
        //perform enhancements
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(newOAuthValue,KongPluginOAuthEnhanced.class));
        return responsePolicy;
    }

    /**
     * Validate Request Transformation Policy
     *
     * @param policy
     * @return
     */
    public static synchronized Policy validateRequestTransformer(Policy policy){
        Gson gson = new Gson();
        KongPluginRequestTransformer reqTransValue = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginRequestTransformer.class);
        KongPluginRequestTransformer newTransValue = new KongPluginRequestTransformer();
        //remove null values
        reqTransValue.getAdd().getForm().stream().forEach((val) -> {if(val!=null)newTransValue.getAdd().getForm().add(val);});
        reqTransValue.getAdd().getHeaders().stream().forEach((val) -> {if(val!=null)newTransValue.getAdd().getForm().add(val);});
        reqTransValue.getAdd().getQuerystring().stream().forEach((val) -> {if(val!=null)newTransValue.getAdd().getForm().add(val);});
        reqTransValue.getRemove().getForm().stream().forEach((val) -> {if(val!=null)newTransValue.getRemove().getForm().add(val);});
        reqTransValue.getRemove().getHeaders().stream().forEach((val) -> {if(val!=null)newTransValue.getRemove().getForm().add(val);});
        reqTransValue.getRemove().getQuerystring().stream().forEach((val) -> {if(val!=null)newTransValue.getRemove().getForm().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(newTransValue));
        return policy;
    }

    public static synchronized Policy validateResponseTransformer(Policy policy){
        return policy;
    }

    public static synchronized Policy validateBasicAuth(Policy policy){
        return policy;
    }
    public static synchronized Policy validateCORS(Policy policy){
        return policy;
    }
    public static synchronized Policy validateFileLog(Policy policy){
        return policy;
    }
    public static synchronized Policy validateHTTPLog(Policy policy){
        return policy;
    }
    public static synchronized Policy validateUDPLog(Policy policy){
        return policy;
    }
    public static synchronized Policy validateTCPLog(Policy policy){
        return policy;
    }
    public static synchronized Policy validateIPRestriction(Policy policy){
        return policy;
    }
    public static synchronized Policy validateKeyAuth(Policy policy){
        return policy;
    }
    public static synchronized Policy validateRateLimiting(Policy policy){
        return policy;
    }
    public static synchronized Policy validateRequestSizeLimiting(Policy policy){
        return policy;
    }
    public static synchronized Policy validateSSL(Policy policy){
        return policy;
    }
    public static synchronized Policy validateAnalytics(Policy policy){
        return policy;
    }
}
