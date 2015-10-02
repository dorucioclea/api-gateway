package com.t1t.digipolis.apim.gateway.rest;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PolicyViolationException;
import com.t1t.digipolis.kong.model.KongPluginAnalytics;
import com.t1t.digipolis.kong.model.KongPluginCors;
import com.t1t.digipolis.kong.model.KongPluginFileLog;
import com.t1t.digipolis.kong.model.KongPluginHttpLog;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuthEnhanced;
import com.t1t.digipolis.kong.model.KongPluginOAuthScope;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformerAdd;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformerRemove;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformer;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformerAdd;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformerRemove;
import com.t1t.digipolis.kong.model.KongPluginTcpLog;
import com.t1t.digipolis.kong.model.KongPluginUdpLog;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michallispashidis on 30/09/15.
 */
public class GatewayValidation {

    public static Policy validate(Policy policy) throws PolicyViolationException{
        //verify policy def that applies
        Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
        switch(policies){
            //all policies can be available here
            case BASICAUTHENTICATION: return validateBasicAuth(policy);
            case CORS: return validateCORS(policy);
            case FILELOG: return validateFileLog(policy);
            case HTTPLOG: return validateHTTPLog(policy);
            case UDPLOG: return validateUDPLog(policy);
            case TCPLOG: return validateTCPLog(policy);
            case IPRESTRICTION: return validateIPRestriction(policy);
            case KEYAUTHENTICATION: return validateKeyAuth(policy);
            case OAUTH2: return validateOAuth(policy);
            case RATELIMITING: return validateRateLimiting(policy);
            case REQUESTSIZELIMITING: return validateRequestSizeLimiting(policy);
            case REQUESTTRANSFORMER: return validateRequestTransformer(policy);
            case RESPONSETRANSFORMER: return validateResponseTransformer(policy);
            case SSL: return validateSSL(policy);
            case ANALYTICS: return validateAnalytics(policy);
            default:throw new PolicyViolationException("Unknown policy "+ policy);
        }
    }

    /**
     * OAuth is a special case, we want to keep the scopes until the gateway applies the policy.
     *
     * @param policy
     * @return
     */
    public static synchronized Policy validateOAuth(Policy policy){
        return policy;
    }

    /**
     * Validate OAuth plugin values and if necessary transform.
     *
     * @param policy    OAuth policy
     * @return
     */
    public static synchronized Policy validateExplicitOAuth(Policy policy) {
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
        KongPluginRequestTransformer req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginRequestTransformer.class);
        KongPluginRequestTransformer res = new KongPluginRequestTransformer();
        KongPluginRequestTransformerAdd addStatement = new KongPluginRequestTransformerAdd();
        addStatement.setForm(new ArrayList<>());
        addStatement.setHeaders(new ArrayList<>());
        addStatement.setQuerystring(new ArrayList<>());
        com.t1t.digipolis.kong.model.KongPluginRequestTransformerRemove remStatement = new KongPluginRequestTransformerRemove();
        remStatement.setForm(new ArrayList<>());
        remStatement.setHeaders(new ArrayList<>());
        remStatement.setQuerystring(new ArrayList<>());
        res.setAdd(addStatement);
        res.setRemove(remStatement);
        //remove null values
        req.getAdd().getForm().stream().forEach((val) -> {if (val != null) res.getAdd().getForm().add(val);});
        req.getAdd().getHeaders().stream().forEach((val) -> {if(val!=null)res.getAdd().getHeaders().add(val);});
        req.getAdd().getQuerystring().stream().forEach((val) -> {if(val!=null)res.getAdd().getQuerystring().add(val);});
        req.getRemove().getForm().stream().forEach((val) -> {if(val!=null)res.getRemove().getForm().add(val);});
        req.getRemove().getHeaders().stream().forEach((val) -> {if(val!=null)res.getRemove().getHeaders().add(val);});
        req.getRemove().getQuerystring().stream().forEach((val) -> {if(val!=null)res.getRemove().getQuerystring().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        return responsePolicy;
    }

    public static synchronized Policy validateResponseTransformer(Policy policy){
        Gson gson = new Gson();
        KongPluginResponseTransformer req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginResponseTransformer.class);
        KongPluginResponseTransformer res = new KongPluginResponseTransformer();
        KongPluginResponseTransformerAdd addStatement = new KongPluginResponseTransformerAdd();
        addStatement.setHeader(new ArrayList<>());
        addStatement.setJson(new ArrayList<>());
        KongPluginResponseTransformerRemove remStatement = new KongPluginResponseTransformerRemove();
        remStatement.setHeader(new ArrayList<>());
        remStatement.setJson(new ArrayList<>());
        res.setAdd(addStatement);
        res.setRemove(remStatement);
        //remove null values
        req.getAdd().getHeader().stream().forEach((val) -> {if (val != null) res.getAdd().getHeader().add(val);});
        req.getAdd().getJson().stream().forEach((val) -> {if(val!=null)res.getAdd().getJson().add(val);});
        req.getRemove().getHeader().stream().forEach((val) -> {if(val!=null)res.getRemove().getHeader().add(val);});
        req.getRemove().getJson().stream().forEach((val) -> {if(val!=null)res.getRemove().getJson().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        return responsePolicy;
    }

    public static synchronized Policy validateBasicAuth(Policy policy){
        return policy;
    }

    public static synchronized Policy validateCORS(Policy policy){
        Gson gson = new Gson();
        KongPluginCors req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginCors.class);
        KongPluginCors res = new KongPluginCors();
        res.setExposedHeaders(new ArrayList<>());
        res.setHeaders(new ArrayList<>());
        res.setMethods(new ArrayList<>());
        req.getExposedHeaders().stream().forEach(val -> {if (!StringUtils.isEmpty(val)) res.getExposedHeaders().add(val);});
        req.getHeaders().stream().forEach(val->{if(!StringUtils.isEmpty(val))res.getHeaders().add(val);});
        req.getMethods().stream().forEach(val->{if(val!=null)res.getMethods().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        return responsePolicy;
    }

    public static synchronized Policy validateFileLog(Policy policy){
        Gson gson = new Gson();
        KongPluginFileLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginFileLog.class);
        if(StringUtils.isEmpty(req.getPath())) throw new PolicyViolationException("Form was not correctly filled in.");
        return policy;
    }

    public static synchronized Policy validateHTTPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginHttpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginHttpLog.class);
        if(StringUtils.isEmpty(req.getHttpEndpoint())) throw new PolicyViolationException("Form was not correctly filled in.");
        return policy;
    }

    public static synchronized Policy validateUDPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginUdpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginUdpLog.class);
        if(StringUtils.isEmpty(req.getHost())) throw new PolicyViolationException("Form was not correctly filled in.");
        return policy;
    }

    public static synchronized Policy validateTCPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginTcpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginTcpLog.class);
        if(StringUtils.isEmpty(req.getHost())) throw new PolicyViolationException("Form was not correctly filled in.");
        return policy;
    }

    public static synchronized Policy validateIPRestriction(Policy policy){
        Gson gson = new Gson();
        KongPluginIPRestriction req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginIPRestriction.class);
        KongPluginIPRestriction res = new KongPluginIPRestriction();
        res.setBlacklist(new ArrayList<>());
        res.setWhitelist(new ArrayList<>());
        req.getBlacklist().stream().forEach(val->{if(!StringUtils.isEmpty(val))res.getBlacklist().add(val);});
        req.getWhitelist().stream().forEach(val->{if(val!=null)res.getWhitelist().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        return responsePolicy;
    }

    public static synchronized Policy validateKeyAuth(Policy policy){
        KongPluginKeyAuth req = new Gson().fromJson(policy.getPolicyJsonConfig(),KongPluginKeyAuth.class);
        if(req.getKeyNames()!=null&&req.getKeyNames().size()>0)return policy;
        else throw new PolicyViolationException("Default applies already or you should provide at least one key name.");
    }

    public static synchronized Policy validateRateLimiting(Policy policy){
        //nothing to do - works fine
        return policy;
    }

    public static synchronized Policy validateRequestSizeLimiting(Policy policy){
        //nothing to do - works fine
        return policy;
    }

    public static synchronized Policy validateSSL(Policy policy){
        throw new PolicyViolationException("At the moment no DNS services have been registered, you need DNS based routing for SSL to apply.");
    }

    public static synchronized Policy validateAnalytics(Policy policy){
        Gson gson = new Gson();
        KongPluginAnalytics req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginAnalytics.class);
        if(StringUtils.isEmpty(req.getServiceToken())) throw new PolicyViolationException("Form was not correctly filled in.");
        return policy;
    }
}
