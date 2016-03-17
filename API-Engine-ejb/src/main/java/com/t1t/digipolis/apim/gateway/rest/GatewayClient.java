package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongPluginAnalytics;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.kong.model.KongPluginCors;
import com.t1t.digipolis.kong.model.KongPluginHttpLog;
import com.t1t.digipolis.kong.model.KongPluginJWTRequest;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuth;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthEnhanced;
import com.t1t.digipolis.kong.model.KongPluginOAuthScope;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.GatewayPathUtilities;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.gateway.GatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import javax.inject.Inject;
import java.util.*;

/**
 * A REST client for accessing the Gateway API.
 * TODO ACL groups cannot be mixed through API => unique naming
 */
public class GatewayClient {
    private static Logger log = LoggerFactory.getLogger(GatewayClient.class.getName());
    private KongClient httpClient;
    private GatewayBean gatewayBean;
    private IStorage storage;
    private AppConfig appConfig;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static String metricsURI;
    private static String AUTH_API_KEY = "apikey";

    /**
     * Constructor.
     *
     * @param httpClient the http client
     */
    public GatewayClient(KongClient httpClient, GatewayBean gateway, IStorage storage, String metricsURI, AppConfig appConfig) {
        Preconditions.checkNotNull(httpClient);
        Preconditions.checkNotNull(storage);
        Preconditions.checkNotNull(gateway);
        Preconditions.checkNotNull(appConfig);
        this.httpClient = httpClient;
        this.gatewayBean = gateway;
        this.storage = storage;
        this.metricsURI = metricsURI;
        this.appConfig = appConfig;
    }

    public SystemStatus getStatus() throws GatewayAuthenticationException {
        Object kongInfo = httpClient.getInfo();
        KongInfo kongInformation = httpClient.getParsedInfo();
        Object kongStatus = httpClient.getStatus();
        Object kongCluster = httpClient.getCluster();
        Gson gson = new Gson();
        SystemStatus systemStatus = new SystemStatus();
        systemStatus.setDescription(kongInformation.getTagline());
        systemStatus.setVersion(kongInformation.getVersion());
        systemStatus.setName(kongInformation.getHostname());
        systemStatus.setId(kongInformation.getHostname());
        systemStatus.setInfo(gson.toJson(kongInfo));
        systemStatus.setStatus(gson.toJson(kongStatus));
        systemStatus.setCluster(gson.toJson(kongCluster));
        systemStatus.setUp(true);
        return systemStatus;
    }

    public ServiceEndpoint getServiceEndpoint(String basePath, String organizationId, String serviceId, String version) throws GatewayAuthenticationException {
        ServiceEndpoint endpoint = new ServiceEndpoint();
        StringBuilder url = new StringBuilder();
        url.append(gatewayBean.getEndpoint());
        Service service = new Service();
        service.setOrganizationId(organizationId);
        service.setServiceId(serviceId);
        service.setVersion(version);
        service.setBasepath(basePath);
        //TODO set basepath
        url.append(GatewayPathUtilities.generateGatewayContextPath(service));
        endpoint.setEndpoint(url.toString());
        return endpoint;

    }

    /**
     * Register an application is:
     * <ul>
     *     <li>create the consumer (app.version)</li>
     *     <li>create an acl (org.app.version.plan.version) and add to api</li>
     *     <li>add acl group to the whitelist of the api</li>
     *     <li>add client application to the group</li>
     *     <li>add keyauth with API key for specific API and consumer</li>
     * </ul>
     *
     * Each plan should contain the following policies:
     * <ul>
     *     <li>HTTP Log configured for the metrics engine for each service consumer</li>
     *     <li>Key authentication</li>
     * </ul>
     *
     * The following policies can be part of a plan:
     * <ul>
     *     <li>IP Restriction</li>
     *     <li>Rate Limiting</li>
     *     <li>Request size limiting</li>
     * </ul>
     * @param application
     * @throws RegistrationException
     * @throws GatewayAuthenticationException
     */
    public void register(Application application) throws RegistrationException, GatewayAuthenticationException {
        //create consumer
        String consumerId = ConsumerConventionUtil.createAppUniqueId(application.getOrganizationId(), application.getApplicationId(), application.getVersion());
        KongConsumer consumer = httpClient.getConsumer(consumerId);
        KongApi api;
        //context of API
        for(Contract contract:application.getContracts()){
            log.info("Register application with contract:{}",contract);
            //get the API
            String apiName = ServiceConventionUtil.generateServiceUniqueName(contract.getServiceOrgId(), contract.getServiceId(), contract.getServiceVersion());
            api = httpClient.getApi(apiName);
            for(Policy policy:contract.getPolicies()){
                //execute policy
                Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                switch(policies){
                    //all policies can be available here
                    case IPRESTRICTION: createPlanPolicy(api, consumer, policy, Policies.IPRESTRICTION.getKongIdentifier(), Policies.IPRESTRICTION.getClazz());break;
                    case RATELIMITING: createPlanPolicy(api, consumer, policy, Policies.RATELIMITING.getKongIdentifier(), Policies.RATELIMITING.getClazz());break;
                    case REQUESTSIZELIMITING: createPlanPolicy(api, consumer, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(),Policies.REQUESTSIZELIMITING.getClazz());break;
                    default:break;
                }
            }
        }
    }

    public void registerAppConsumer(Application application, KongConsumer appConsumer){
        //register consumer application
        //for each API register consumer on API
        KongApi api;
        //context of API
        for(Contract contract:application.getContracts()){
            //get the API
            String apiName = ServiceConventionUtil.generateServiceUniqueName(contract.getServiceOrgId(), contract.getServiceId(), contract.getServiceVersion());
            api = httpClient.getApi(apiName);
            for(Policy policy:contract.getPolicies()){
                //execute policy
                Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                switch(policies){
                    //all policies can be available here
                    case IPRESTRICTION: createPlanPolicy(api, appConsumer, policy, Policies.IPRESTRICTION.getKongIdentifier(), Policies.IPRESTRICTION.getClazz());break;
                    case RATELIMITING: createPlanPolicy(api, appConsumer, policy, Policies.RATELIMITING.getKongIdentifier(), Policies.RATELIMITING.getClazz());break;
                    case REQUESTSIZELIMITING: createPlanPolicy(api, appConsumer, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(),Policies.REQUESTSIZELIMITING.getClazz());break;
                    default:break;
                }
            }
        }
    }

    public void unregister(String organizationId, String applicationId, String version) throws RegistrationException, GatewayAuthenticationException {
        //remove the application consumer and it's keyauth credentials
        String consumerId = ConsumerConventionUtil.createAppUniqueId(organizationId,applicationId,version);
        KongPluginKeyAuthResponseList credentials = httpClient.getConsumerKeyAuthCredentials(consumerId);
        if(credentials!=null && credentials.getData()!=null && credentials.getData().size()>0){
            for(KongPluginKeyAuthResponse cred:credentials.getData())httpClient.deleteConsumerKeyAuthCredential(consumerId,cred.getId());
        }
        //remove application consumer
        httpClient.deleteConsumer(consumerId);
    }

    /**
     * Publishes an API to the kong gateway.
     * The properties path and target_url are variable, and will be retrieved from the service dto.
     * The properties strip_path, preserve_host and public_dns will be set equally for all registered endpoints
     * The path should be: /organizationname/applicationname/version
     *
     * Default enable:
     * <ul>
     *     <li>Key authentication</li>
     *     <li>CORS</li></loi>
     * </ul>
     *
     * @param service
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public void publish(Service service) throws PublishingException, GatewayAuthenticationException {
        Preconditions.checkNotNull(service);
        //create the service using path, and target_url
        KongApi api = new KongApi();
        api.setStripRequestPath(true);
        //api.setPublicDns();
        String nameAndDNS = ServiceConventionUtil.generateServiceUniqueName(service);
        //name wil be: organization.application.version
        api.setName(nameAndDNS);
        //version wil be: organization.application.version
        api.setRequestHost(nameAndDNS);
        //real URL to target
        api.setUpstreamUrl(service.getEndpoint());
        //context path that will be stripped away
        api.setRequestPath(validateServicePath(service));
        log.info("Send to Kong:{}", api.toString());
        //TODO validate if path exists - should be done in GUI, but here it's possible that another user registered using the same path variable.
        try{
            //If service exists already on the gateway due to an invalid action before, delete the existing with the same name (republish)
            KongApi existingAPI = httpClient.getApi(api.getName());
            if(existingAPI!=null&&!StringUtils.isEmpty(existingAPI.getId())){
                //the API exists already - please override - this is restricted due to the naming convention
                httpClient.deleteApi(api.getId());
            }
        }catch (Exception ex){
            //start new client to comm with kong
            log.info("Warning during service publication: {}",ex.getMessage());
            //continue
        }
        api = httpClient.addApi(api);

        //flag for custom CORS policy
        boolean customCorsFlag = false;
        //flag for custom KeyAuth policy
        boolean customKeyAuth = false;
        //flag for custom HTTP policy
        boolean customHttp = false;
        //flag for OAuth2
        boolean flagOauth2 = false;
        //flag for custom Analytics policy
        boolean customAnalytics = false;
        //verify if api creation has been succesfull
        if(!StringUtils.isEmpty(api.getId())){
            try{
                List<Policy> policyList = service.getServicePolicies();
                for(Policy policy:policyList){
                    //execute policy
                    Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                    switch(policies){
                        //all policies can be available here
                        case BASICAUTHENTICATION: createServicePolicy(api, policy, Policies.BASICAUTHENTICATION.getKongIdentifier(),Policies.BASICAUTHENTICATION.getClazz());break;
                        case CORS: createServicePolicy(api, policy, Policies.CORS.getKongIdentifier(),Policies.CORS.getClazz());customCorsFlag=true;break;
                        case FILELOG: createServicePolicy(api, policy, Policies.FILELOG.getKongIdentifier(),Policies.FILELOG.getClazz());break;
                        case HTTPLOG: createServicePolicy(api,policy, Policies.HTTPLOG.getKongIdentifier(),Policies.HTTPLOG.getClazz());customHttp=true;break;
                        case UDPLOG: createServicePolicy(api, policy, Policies.UDPLOG.getKongIdentifier(),Policies.UDPLOG.getClazz());break;
                        case TCPLOG: createServicePolicy(api, policy, Policies.TCPLOG.getKongIdentifier(),Policies.TCPLOG.getClazz());break;
                        case IPRESTRICTION: createServicePolicy(api, policy, Policies.IPRESTRICTION.getKongIdentifier(),Policies.IPRESTRICTION.getClazz());break;
                        case KEYAUTHENTICATION: createServicePolicy(api, policy, Policies.KEYAUTHENTICATION.getKongIdentifier(),Policies.KEYAUTHENTICATION.getClazz());customKeyAuth=true;break;
                        //for OAuth2 we have an exception, we validate the form data at this moment to keep track of OAuth2 scopes descriptions
                        case OAUTH2: KongPluginConfig config = createServicePolicy(api, GatewayValidation.validateExplicitOAuth(policy), Policies.OAUTH2.getKongIdentifier(), KongPluginOAuthEnhanced.class);
                            log.info("start post oauth2 actions");flagOauth2=true;postOAuth2Actions(service, policy,config);break;//upon transformation we use another enhanced object for json deserialization
                        case RATELIMITING: createServicePolicy(api, policy, Policies.RATELIMITING.getKongIdentifier(),Policies.RATELIMITING.getClazz());break;
                        case REQUESTSIZELIMITING: createServicePolicy(api, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(),Policies.REQUESTSIZELIMITING.getClazz());break;
                        case REQUESTTRANSFORMER: createServicePolicy(api, policy, Policies.REQUESTTRANSFORMER.getKongIdentifier(),Policies.REQUESTTRANSFORMER.getClazz());break;
                        case RESPONSETRANSFORMER: createServicePolicy(api, policy, Policies.RESPONSETRANSFORMER.getKongIdentifier(),Policies.RESPONSETRANSFORMER.getClazz());break;
                        case SSL: createServicePolicy(api, policy, Policies.CORS.getKongIdentifier(),Policies.SSL.getClazz());break;
                        case ANALYTICS: createServicePolicy(api,policy,Policies.ANALYTICS.getKongIdentifier(),Policies.ANALYTICS.getClazz());customAnalytics=true;break;
                        default:break;
                    }
                }
            }catch (Exception e){
                //if anything goes wrong, return exception and rollback api created
                if(api!=null&&!StringUtils.isEmpty(api.getId())){
                    httpClient.deleteApi(api.getId());
                }
            }
        }

        //add default CORS Policy if no custom CORS defined
        if(!customCorsFlag) registerDefaultCORSPolicy(api);
        //don't apply on the UI a API key if OAuth2 enabled
        if(!customKeyAuth&&!flagOauth2) registerDefaultKeyAuthPolicy(api);
        if(!customHttp&&!StringUtils.isEmpty(metricsURI)) registerDefaultHttpPolicy(api);
        //add default Galileo policy
        //if(!customAnalytics)registerDefaultAnalyticsPolicy(api);
        //default enable jwt validation for a service - authorization bearer or jwt fieldname accepted by default
        //registerDefaultJWTPolicy(api);
    }

    /**
     * After applying the OAuth2 plugin to a service the following action gets executed.
     * In case of OAuth2 - add provision_key to the service version, and additional scopes.
     *
     * @param policy
     */
    private void postOAuth2Actions(Service service, Policy policy, KongPluginConfig config) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(policy);
        Preconditions.checkNotNull(config);
        //config contains provisioning and scopes
        try {
            Gson gson = new Gson();
            //retrieve scope info from policy json
            KongPluginOAuth oauthValue = gson.fromJson(policy.getPolicyJsonConfig(), KongPluginOAuth.class);//original request - we need this for the scope descriptions
            KongPluginOAuthEnhanced enhancedOAuthValue = gson.fromJson(config.getConfig().toString(),KongPluginOAuthEnhanced.class);//response from Kong - we need this for the provisioning key
            log.info("Response after applying oauth on API:{}",enhancedOAuthValue);
            ServiceVersionBean svb = storage.getServiceVersion(service.getOrganizationId(), service.getServiceId(), service.getVersion());
            svb.setProvisionKey(enhancedOAuthValue.getProvisionKey());
            Map<String,String> scopeMap = new HashMap<>();
            List<KongPluginOAuthScope> scopeObjects = oauthValue.getScopes();
            for(KongPluginOAuthScope scope:scopeObjects){
                scopeMap.put(scope.getScope(), scope.getScopeDesc());
            }
            svb.setOauthScopes(scopeMap);
            storage.updateServiceVersion(svb);
        } catch (StorageException e) {
            throw new GatewayException("Error update service version bean with OAuth2 info:"+e.getMessage());
        }
    }

    /**
     * Registers the default httplog plugin pointing to the metrics server.
     * We cannot add 2x HTTP logs instances.
     * @param api
     */
    private void registerDefaultHttpPolicy(KongApi api) {
        KongPluginHttpLog httpPolicy = new KongPluginHttpLog()
                .withHttpEndpoint(metricsURI)
                .withMethod(KongPluginHttpLog.Method.POST);
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.HTTPLOG.getKongIdentifier())
                .withConfig(httpPolicy);
        httpClient.createPluginConfig(api.getId(),config);
    }

    private void registerDefaultAnalyticsPolicy(KongApi api){
        if(appConfig.getAnalyticsEnabled()){
            KongPluginAnalytics analyticsPolicy = new KongPluginAnalytics()
                    .withBatchSize(appConfig.getAnalyticsBatchSize())
                    .withDelay(appConfig.getAnalyticsDelay())
                    .withEnvironment(appConfig.getAnalyticsEnvironment())
                    .withHost(appConfig.getAnalyticsHost())
                    .withPort(appConfig.getAnalyticsPort())
                    .withLogBody(appConfig.getAnalyticsLogBody())
                    .withMaxSendingQueueSize(appConfig.getAnalyticsMaxSendingQueue())
                    .withServiceToken(appConfig.getAnalyticsServiceToken());
            KongPluginConfig config = new KongPluginConfig()
                    .withName(Policies.ANALYTICS.getKongIdentifier())
                    .withConfig(analyticsPolicy);
            httpClient.createPluginConfig(api.getId(),config);
        }
    }

    /**
     * Registers the default keyauth plugin with apikey key_value (service-scoped policy).
     * Only consumers having an valid API key can access the API.
     * TODO Kong 0.5.0 - add ACL group
     * @param api
     */
    private void registerDefaultKeyAuthPolicy(KongApi api) {
        KongPluginKeyAuth keyAuthPolicy = new KongPluginKeyAuth()
                .withKeyNames(Arrays.asList(AUTH_API_KEY));
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.KEYAUTHENTICATION.getKongIdentifier())
                .withConfig(keyAuthPolicy);
        httpClient.createPluginConfig(api.getId(),config);
    }

    /**
     * Register the default JWT plugin with self-generated key and security.
     * @param api
     */
    private void registerDefaultJWTPolicy(KongApi api) {
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.JWT.getKongIdentifier());
        httpClient.createPluginConfig(api.getId(),config);
    }

    /**
     * Registers the default CORS for the service (service-scoped policy)
     * @param api
     */
    private void registerDefaultCORSPolicy(KongApi api) {
        List<String> headers = Arrays.asList("Accept", "Accept-Version", "Content-Length", "Content-MD5", "Content-Type", "Date", AUTH_API_KEY, "Authorization");
        KongPluginCors corsPolicy = new KongPluginCors(); //default values are ok
        corsPolicy.setHeaders(headers);
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.CORS.getKongIdentifier())
                .withConfig(corsPolicy);
        httpClient.createPluginConfig(api.getId(),config);
    }

    /**
     * Validates the service path property before passing it to Kong.
     *
     * @param service
     * @return
     */
    private String validateServicePath(Service service) {
        return GatewayPathUtilities.generateGatewayContextPath(service);
    }

    /**
     * The retire functionality removes the api and deletes the policies that are applied on the api.
     * The policies are removed as a responsability of Kong gateway.
     * @param organizationId
     * @param serviceId
     * @param version
     * @throws RegistrationException
     * @throws GatewayAuthenticationException
     */
    public void retire(String organizationId, String serviceId, String version) throws RegistrationException, GatewayAuthenticationException {
        Preconditions.checkArgument(!StringUtils.isEmpty(organizationId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        //create the service using path, and target_url
        String nameAndDNS = ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version);
        httpClient.deleteApi(nameAndDNS);
    }

    public KongConsumer getConsumer(String id){
        KongConsumer responseConsumer = new KongConsumer();
        try {
            responseConsumer = httpClient.getConsumer(id);
            return responseConsumer;
        }catch (RetrofitError err){
            //user is not existing
            return null;
        }
    }

    public KongConsumer createConsumer(String userUniqueName){
        return httpClient.createConsumer(new KongConsumer().withUsername(userUniqueName).withCustomId(userUniqueName));
    }

    public KongConsumer createConsumerWithCustomID(String customId){
        return httpClient.createConsumer(new KongConsumer().withCustomId(customId));
    }

    public KongConsumer createConsumer(String userUniqueId, String customId){
        return httpClient.createConsumer(new KongConsumer().withUsername(userUniqueId).withCustomId(customId));
    }

    public KongPluginKeyAuthResponseList getConsumerKeyAuth(String id){
        return httpClient.getConsumerKeyAuthCredentials(id);
    }

    public KongPluginKeyAuthResponse createConsumerKeyAuth(String id){
        return httpClient.createConsumerKeyAuthCredentials(id, new KongPluginKeyAuthRequest());
    }

    public KongPluginKeyAuthResponse createConsumerKeyAuth(String id, String apiKey){
        return httpClient.createConsumerKeyAuthCredentials(id, new KongPluginKeyAuthRequest().withKey(apiKey));
    }

    public KongPluginJWTResponse createConsumerJWT(String id){
        return httpClient.createConsumerJWTCredentials(id, new KongPluginJWTRequest());
    }

    public KongPluginJWTResponseList getConsumerJWT(String id){
        return httpClient.getConsumerJWTCredentials(id);
    }

    public KongPluginConfigList getServicePlugins(String serviceId){
        return httpClient.getKongPluginConfigList(serviceId);
    }

    public KongPluginConfig getServicePlugin(String serviceId, String pluginId){
        return httpClient.getKongPluginConfig(serviceId, pluginId);
    }

    public KongPluginConfig updateServicePlugin(String serviceId, KongPluginConfig config){
        httpClient.updateKongPluginConfig(serviceId,config);
        return config;
    }

    public void deleteConsumerKeyAuth(String id, String apikey){
        //get all registered api key values for a consumer
        KongPluginKeyAuthResponseList keyAuthCredentials = httpClient.getConsumerKeyAuthCredentials(id);
        if(keyAuthCredentials!=null & keyAuthCredentials.getData()!=null && keyAuthCredentials.getData().size()>0){
            for(KongPluginKeyAuthResponse cred : keyAuthCredentials.getData()){
                if(cred.getKey().equals(apikey))httpClient.deleteConsumerKeyAuthCredential(id,cred.getId());
            }
        }
    }

    public KongPluginBasicAuthResponse createConsumerBasicAuth(String userId, String userLoginName, String userPassword ){
        return httpClient.createConsumerBasicAuthCredentials(userId, new KongPluginBasicAuthRequest().withUsername(userLoginName).withPassword(userPassword));
    }

    public KongPluginBasicAuthResponseList getConsumerBasicAuth(String id){
        return httpClient.getConsumerBasicAuthCredentials(id);
    }

    public KongApi getApi(String id){
        return httpClient.getApi(id);
    }

    public void deleteConsumer(String id){
        httpClient.deleteConsumer(id);
    }

    public KongPluginOAuthConsumerResponse enableConsumerForOAuth(String consumerId,KongPluginOAuthConsumerRequest request){
        //be sure that the uri ends with an '/'
        if(!request.getRedirectUri().endsWith("/"))request.setRedirectUri(request.getRedirectUri() + "/");
        return httpClient.enableOAuthForConsumer(consumerId,request.getName(),request.getClientId(),request.getClientSecret(),request.getRedirectUri());
    }

    public KongPluginOAuthConsumerResponse updateConsumerOAuthCredentials(String consumerId, KongPluginOAuthConsumerRequest request) {
        if(!request.getRedirectUri().endsWith("/"))request.setRedirectUri(request.getRedirectUri() + "/");
        return httpClient.updateOAuthForConsumer(consumerId,request.getName(),request.getClientId(),request.getClientSecret(),request.getRedirectUri());
    }

    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(String clientId){
        return httpClient.getApplicationOAuthInformation(clientId);
    }

    public KongPluginOAuthConsumerResponseList getConsumerOAuthCredentials(String consumerId){
        return httpClient.getConsumerOAuthCredentials(consumerId);
    }

    public void deleteOAuthConsumerCredential(String consumerId, String oauthPluginId){
        httpClient.deleteOAuth2Credential(consumerId, oauthPluginId);
    }

    /*Service policies*/

    /**
     * This method creates the policy on given api with pre-defined values.
     *
     * @param api
     * @param policy
     * @param kongIdentifier
     * @param clazz
     * @param <T>
     */
    private <T extends KongConfigValue> KongPluginConfig createServicePolicy(KongApi api, Policy policy, String kongIdentifier,Class<T> clazz)throws PublishingException {
        Gson gson = new Gson();
        //perform value mapping
        KongConfigValue plugin = gson.fromJson(policy.getPolicyJsonConfig(), clazz);
        KongPluginConfig config = new KongPluginConfig()
                .withName(kongIdentifier)//set required kong identifier
                .withConfig(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        config = httpClient.createPluginConfig(api.getId(),config);
        return config;
    }

    /**
     * This method creates a policy for a given plan, applied on an API.
     *
     * @param api
     * @param policy
     * @param kongIdentifier
     * @param clazz
     * @param <T>
     * @throws PublishingException
     */
    private <T extends KongConfigValue> void createPlanPolicy(KongApi api, KongConsumer consumer, Policy policy, String kongIdentifier,Class<T> clazz)throws PublishingException {
        Gson gson = new Gson();
        //perform value mapping
        KongConfigValue plugin = gson.fromJson(policy.getPolicyJsonConfig(), clazz);
        KongPluginConfig config = new KongPluginConfig()
                .withName(kongIdentifier)//set required kong identifier
                .withConsumerId(consumer.getId())
                .withConfig(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        //execute
        config = httpClient.createPluginConfig(api.getId(),config);
    }

    public static String getMetricsURI() {
        return metricsURI;
    }
}
