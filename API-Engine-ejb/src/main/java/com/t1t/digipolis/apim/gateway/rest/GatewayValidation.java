package com.t1t.digipolis.apim.gateway.rest;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.jwt.JWTFormBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.exceptions.PolicyDefinitionInvalidException;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PolicyViolationException;
import com.t1t.digipolis.kong.model.KongPluginAnalytics;
import com.t1t.digipolis.kong.model.KongPluginCors;
import com.t1t.digipolis.kong.model.KongPluginJWT;
import com.t1t.digipolis.kong.model.KongPluginFileLog;
import com.t1t.digipolis.kong.model.KongPluginHttpLog;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuthEnhanced;
import com.t1t.digipolis.kong.model.KongPluginOAuthScope;
import com.t1t.digipolis.kong.model.KongPluginRateLimiting;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformerAdd;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformerRemove;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformer;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformerAdd;
import com.t1t.digipolis.kong.model.KongPluginResponseTransformerRemove;
import com.t1t.digipolis.kong.model.KongPluginTcpLog;
import com.t1t.digipolis.kong.model.KongPluginUdpLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by michallispashidis on 30/09/15.
 */
public class GatewayValidation {
    private static Logger _LOG = LoggerFactory.getLogger(GatewayValidation.class.getName());
    private static String environment;
    @Inject
    private AppConfig config;
    {
        environment = "";
        if(config!=null){
            environment = new StringBuffer("").append(config.getEnvironment()).toString();
        }
    }

    public static Policy validate(Policy policy) throws PolicyViolationException{
        _LOG.debug("Valdiate policy:{}",policy);
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
            case JWT: return validateJWT(policy);
            default:throw new PolicyViolationException("Unknown policy "+ policy);
        }
    }

    private static Policy validateJWT(Policy policy) {
        Gson gson = new Gson();
        JWTFormBean jwtValue = gson.fromJson(policy.getPolicyJsonConfig(),JWTFormBean.class);
        KongPluginJWT kongPluginJWT = new KongPluginJWT();
        List<String> claimsToVerify = new ArrayList<>();
        //if(jwtValue.getClaims_to_verify())claimsToVerify.add("exp");//hardcoded claim at the moment
        //--enforce to validate JWT exp
        claimsToVerify.add("exp");
        kongPluginJWT.setClaimsToVerify(claimsToVerify);
        //perform enhancements
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(kongPluginJWT,KongPluginJWT.class));
        _LOG.debug("Modified policy:{}",policy);
        return responsePolicy;
    }

    /**
     * OAuth is a special case, we want to keep the scopes until the gateway applies the policy.
     *
     * @param policy
     * @return
     */
    public static synchronized Policy validateOAuth(Policy policy){
        Gson gson = new Gson();
        KongPluginOAuth oauthValue = gson.fromJson(policy.getPolicyJsonConfig(), KongPluginOAuth.class);
        if(oauthValue.getScopes().size()==0)throw new PolicyViolationException("Scopes/scopes description must be provided in order to apply OAuth2");
        //create custom provisionkey - explicitly
        oauthValue.setProvisionKey(UUID.randomUUID().toString());
        _LOG.debug("Modified policy:{}",policy);
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
        _LOG.debug("Modified policy:{}",policy);
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
        //if all lists are empty -> error
        if(isEmptyList(req.getAdd().getForm())&&isEmptyList(req.getAdd().getHeaders())&&isEmptyList(req.getAdd().getQuerystring())
                &&isEmptyList(req.getRemove().getForm())&&isEmptyList(req.getRemove().getHeaders())&&isEmptyList(req.getRemove().getQuerystring()))
            throw new PolicyViolationException("At least one value should be provided.");
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
        req.getAdd().getForm().stream().forEach((val) -> {
            if (val != null) res.getAdd().getForm().add(val);
        });
        req.getAdd().getHeaders().stream().forEach((val) -> {
            if (val != null) res.getAdd().getHeaders().add(val);
        });
        req.getAdd().getQuerystring().stream().forEach((val) -> {
            if (val != null) res.getAdd().getQuerystring().add(val);
        });
        req.getRemove().getForm().stream().forEach((val) -> {if(val!=null)res.getRemove().getForm().add(val);});
        req.getRemove().getHeaders().stream().forEach((val) -> {if(val!=null)res.getRemove().getHeaders().add(val);});
        req.getRemove().getQuerystring().stream().forEach((val) -> {if(val!=null)res.getRemove().getQuerystring().add(val);});
        //remove empty lists
        if(res.getAdd().getForm().size()==0)res.getAdd().setForm(null);
        if(res.getAdd().getHeaders().size()==0)res.getAdd().setHeaders(null);
        if(res.getAdd().getQuerystring().size()==0)res.getAdd().setQuerystring(null);
        if(res.getRemove().getForm().size()==0)res.getRemove().setForm(null);
        if(res.getRemove().getHeaders().size()==0)res.getRemove().setHeaders(null);
        if(res.getRemove().getQuerystring().size()==0)res.getRemove().setQuerystring(null);
        Policy requestPolicy = new Policy();
        requestPolicy.setPolicyImpl(policy.getPolicyImpl());
        requestPolicy.setPolicyJsonConfig(gson.toJson(res));
        _LOG.debug("Modified policy:{}",policy);
        return requestPolicy;
    }

    public static synchronized Policy validateResponseTransformer(Policy policy){
        Gson gson = new Gson();
        KongPluginResponseTransformer req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginResponseTransformer.class);
        //if all lists are empty -> error
        if(isEmptyList(req.getAdd().getHeaders())&&isEmptyList(req.getAdd().getJson())
                &&isEmptyList(req.getRemove().getHeaders())&&isEmptyList(req.getRemove().getJson()))
            throw new PolicyViolationException("At least one value should be provided.");
        KongPluginResponseTransformer res = new KongPluginResponseTransformer();
        KongPluginResponseTransformerAdd addStatement = new KongPluginResponseTransformerAdd();
        addStatement.setHeaders(new ArrayList<>());
        addStatement.setJson(new ArrayList<>());
        KongPluginResponseTransformerRemove remStatement = new KongPluginResponseTransformerRemove();
        remStatement.setHeaders(new ArrayList<>());
        remStatement.setJson(new ArrayList<>());
        res.setAdd(addStatement);
        res.setRemove(remStatement);
        //remove null values
        req.getAdd().getHeaders().stream().forEach((val) -> {if (val != null) res.getAdd().getHeaders().add(val);});
        req.getAdd().getJson().stream().forEach((val) -> {if(val!=null)res.getAdd().getJson().add(val);});
        req.getRemove().getHeaders().stream().forEach((val) -> {if(val!=null)res.getRemove().getHeaders().add(val);});
        req.getRemove().getJson().stream().forEach((val) -> {if(val!=null)res.getRemove().getJson().add(val);});
        //remove empty lists
        if(res.getAdd().getHeaders().size()==0)res.getAdd().setHeaders(null);
        if(res.getAdd().getJson().size()==0)res.getAdd().setJson(null);
        if(res.getRemove().getHeaders().size()==0)res.getRemove().setHeaders(null);
        if(res.getRemove().getJson().size()==0)res.getRemove().setJson(null);
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        _LOG.debug("Modified policy:{}",policy);
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
        _LOG.debug("Modified policy:{}",policy);
        return responsePolicy;
    }

    public static synchronized Policy validateFileLog(Policy policy){
        Gson gson = new Gson();
        KongPluginFileLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginFileLog.class);
        if(StringUtils.isEmpty(req.getPath())) throw new PolicyViolationException("Form was not correctly filled in.");
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateHTTPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginHttpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginHttpLog.class);
        if(StringUtils.isEmpty(req.getHttpEndpoint())) throw new PolicyViolationException("Form was not correctly filled in.");
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateUDPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginUdpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginUdpLog.class);
        if(StringUtils.isEmpty(req.getHost())) throw new PolicyViolationException("Form was not correctly filled in.");
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateTCPLog(Policy policy){
        Gson gson = new Gson();
        KongPluginTcpLog req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginTcpLog.class);
        if(StringUtils.isEmpty(req.getHost())) throw new PolicyViolationException("Form was not correctly filled in.");
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateIPRestriction(Policy policy){
        Gson gson = new Gson();
        KongPluginIPRestriction req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginIPRestriction.class);
        //if lists empty -> error
        if(isEmptyList(req.getBlacklist())&&isEmptyList(req.getWhitelist()))throw new PolicyViolationException("At least one value should be provided.");
        KongPluginIPRestriction res = new KongPluginIPRestriction();
        res.setBlacklist(new ArrayList<>());
        res.setWhitelist(new ArrayList<>());
        req.getBlacklist().stream().forEach(val->{if(!StringUtils.isEmpty(val))res.getBlacklist().add(val);});
        req.getWhitelist().stream().forEach(val->{if(val!=null)res.getWhitelist().add(val);});
        Policy responsePolicy = new Policy();
        responsePolicy.setPolicyImpl(policy.getPolicyImpl());
        responsePolicy.setPolicyJsonConfig(gson.toJson(res));
        _LOG.debug("Modified policy:{}",policy);
        return responsePolicy;
    }

    public static synchronized Policy validateKeyAuth(Policy policy){
        KongPluginKeyAuth req = new Gson().fromJson(policy.getPolicyJsonConfig(),KongPluginKeyAuth.class);
        if(req.getKeyNames()!=null&&req.getKeyNames().size()>0){
            _LOG.debug("Modified policy:{}",policy);
            return policy;
        }
        else throw new PolicyViolationException("Default applies already or you should provide at least one key name.");
    }

    public static synchronized Policy validateRateLimiting(Policy policy){
        KongPluginRateLimiting req = new Gson().fromJson(policy.getPolicyJsonConfig(),KongPluginRateLimiting.class);
        List<Integer> ratesArray = new ArrayList<>();
        if (req.getYear() != null) ratesArray.add(req.getYear());
        if (req.getMonth() != null) ratesArray.add(req.getMonth());
        if (req.getDay() != null) ratesArray.add(req.getDay());
        if (req.getHour() != null) ratesArray.add(req.getHour());
        if (req.getMinute() != null) ratesArray.add(req.getMinute());
        if (req.getSecond() != null) ratesArray.add(req.getSecond());
        for (int i = 0; i < ratesArray.size(); i++) {
            for (int j = i + 1; j < ratesArray.size(); j++) {
                if (ratesArray.get(i) > 0 && ratesArray.get(j) > ratesArray.get(i)) {
                    throw new PolicyDefinitionInvalidException("Rates for higher order granularities must be higher than or equal to those of lower orders");
                }
            }
        }
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateRequestSizeLimiting(Policy policy){
        //nothing to do - works fine
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    public static synchronized Policy validateSSL(Policy policy){
        throw new PolicyViolationException("At the moment no DNS services have been registered, you need DNS based routing for SSL to apply.");
    }

    public static synchronized Policy validateAnalytics(Policy policy){
        Gson gson = new Gson();
        KongPluginAnalytics req = gson.fromJson(policy.getPolicyJsonConfig(),KongPluginAnalytics.class);
        if(StringUtils.isEmpty(req.getServiceToken())) throw new PolicyViolationException("Form was not correctly filled in.");
        //implicit environment set -> separate environment support in Mashape analytics - Galileo
        if(!StringUtils.isEmpty(environment))req.setEnvironment(environment);
        _LOG.debug("Modified policy:{}",policy);
        return policy;
    }

    private static boolean isEmptyList(List list){
        return (list==null||list.size()==0);
    }
}
