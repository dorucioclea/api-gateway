package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginCors;
import com.t1t.digipolis.kong.model.KongPluginHttpLog;
import com.t1t.digipolis.kong.model.KongPluginKeyAuth;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthRequest;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.GatewayPathUtilities;
import com.t1t.digipolis.util.ServiceConventionUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.util.Arrays;
import java.util.List;

/**
 * A REST client for accessing the Gateway API.
 * TODO ACL groups cannot be mixed through API => unique naming
 */
@SuppressWarnings("javadoc") // class is temporarily delinked from its interfaces
public class GatewayClient { /*implements ISystemResource, IServiceResource, IApplicationResource*/
    private static Logger log = LoggerFactory.getLogger(GatewayClient.class.getName());
    private KongClient httpClient;
    private GatewayBean gatewayBean;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Config config;
    private static String metricsURI;
    private static String AUTH_API_KEY = "apikey";

    static {
        metricsURI = null;
        config = ConfigFactory.load();
        if(config!=null){
            metricsURI = new StringBuffer("")
                    .append(config.getString(IConfig.METRICS_SCHEME))
                    .append("://")
                    .append(config.getString(IConfig.METRICS_DNS))
                    .append((!StringUtils.isEmpty(config.getString(IConfig.METRICS_PORT)))?":"+config.getString(IConfig.METRICS_PORT):"")
                    .append("/").toString();
        }
    }

    /**
     * Constructor.
     *
     * @param httpClient the http client
     */
    public GatewayClient(KongClient httpClient, GatewayBean gateway) {
        this.httpClient = httpClient;
        this.gatewayBean = gateway;
    }

    public SystemStatus getStatus() throws GatewayAuthenticationException {
        KongInfo kongInformation = httpClient.getInfo();
        SystemStatus systemStatus = new SystemStatus();
        systemStatus.setDescription(kongInformation.getTagline());
        systemStatus.setVersion(kongInformation.getVersion());
        systemStatus.setName(kongInformation.getHostname());
        systemStatus.setId(kongInformation.getHostname());
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
        KongConsumer consumer = new KongConsumer()
                .withUsername(ConsumerConventionUtil.createAppUniqueId(application.getOrganizationId(),application.getApplicationId(),application.getVersion()));
        consumer = httpClient.createConsumer(consumer);
        //register consumer application
        //for each API register keyauth apikey for consumer on API
        KongPluginKeyAuthRequest keyAuthRequest;
        KongApi api;
        //context of API
        for(Contract contract:application.getContracts()){
            keyAuthRequest = new KongPluginKeyAuthRequest().withKey(contract.getApiKey());
            httpClient.createConsumerKeyAuthCredentials(consumer.getId(), keyAuthRequest);
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
        //for each API register keyauth apikey for consumer on API
        KongPluginKeyAuthRequest keyAuthRequest;
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
        //When an API is remove all attached policies are removed as well
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
        api.setStripPath(true);
        //api.setPublicDns();
        String nameAndDNS = ServiceConventionUtil.generateServiceUniqueName(service);
        //name wil be: organization.application.version
        api.setName(nameAndDNS);
        //version wil be: organization.application.version
        api.setPublicDns(nameAndDNS);
        //real URL to target
        api.setTargetUrl(service.getEndpoint());
        //context path that will be stripped away
        api.setPath(validateServicePath(service));
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
                        case HTTPLOG: createServicePolicy(api, policy, Policies.HTTPLOG.getKongIdentifier(),Policies.HTTPLOG.getClazz());customHttp=true;break;
                        case UDPLOG: createServicePolicy(api, policy, Policies.UDPLOG.getKongIdentifier(),Policies.UDPLOG.getClazz());break;
                        case TCPLOG: createServicePolicy(api, policy, Policies.TCPLOG.getKongIdentifier(),Policies.TCPLOG.getClazz());break;
                        case IPRESTRICTION: createServicePolicy(api, policy, Policies.IPRESTRICTION.getKongIdentifier(),Policies.IPRESTRICTION.getClazz());break;
                        case KEYAUTHENTICATION: createServicePolicy(api, policy, Policies.KEYAUTHENTICATION.getKongIdentifier(),Policies.KEYAUTHENTICATION.getClazz());customKeyAuth=true;break;
                        case OAUTH2: createServicePolicy(api, policy, Policies.OAUTH2.getKongIdentifier(),Policies.OAUTH2.getClazz());break;
                        case RATELIMITING: createServicePolicy(api, policy, Policies.RATELIMITING.getKongIdentifier(),Policies.RATELIMITING.getClazz());break;
                        case REQUESTSIZELIMITING: createServicePolicy(api, policy, Policies.REQUESTSIZELIMITING.getKongIdentifier(),Policies.REQUESTSIZELIMITING.getClazz());break;
                        case REQUESTTRANSFORMER: createServicePolicy(api, policy, Policies.REQUESTTRANSFORMER.getKongIdentifier(),Policies.REQUESTTRANSFORMER.getClazz());break;
                        case RESPONSETRANSFORMER: createServicePolicy(api, policy, Policies.RESPONSETRANSFORMER.getKongIdentifier(),Policies.RESPONSETRANSFORMER.getClazz());break;
                        case SSL: createServicePolicy(api, policy, Policies.CORS.getKongIdentifier(),Policies.SSL.getClazz());break;
                        case ANALYTICS: createServicePolicy(api,policy,Policies.ANALYTICS.getKongIdentifier(),Policies.ANALYTICS.getClazz());break;
                        default:break;
                    }
                }
            }catch (Exception e){
                //if anything goes wrong, return exception and rollback api created
                if(api!=null&&!StringUtils.isEmpty(api.getId())){
                    httpClient.deleteApi(api.getId());
                }
                throw new PublishingException(e.getMessage());
            }
        }

        //add default CORS Policy if no custom CORS defined
        if(!customCorsFlag) registerDefaultCORSPolicy(api);
        if(!customKeyAuth) registerDefaultKeyAuthPolicy(api);
        if(!customHttp&&!StringUtils.isEmpty(metricsURI)) registerDefaultHttpPolicy(api);
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
                .withValue(httpPolicy);
        httpClient.createPluginConfig(api.getId(),config);
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
                .withValue(keyAuthPolicy);
        httpClient.createPluginConfig(api.getId(),config);
    }

    /**
     * Registers the default CORS for the service (service-scoped policy)
     * @param api
     */
    private void registerDefaultCORSPolicy(KongApi api) {
        List<Object> headers = Arrays.asList("Accept", "Accept-Version", "Content-Length", "Content-MD5", "Content-Type", "Date", AUTH_API_KEY);
        KongPluginCors corsPolicy = new KongPluginCors(); //default values are ok
        corsPolicy.setHeaders(headers);
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.CORS.getKongIdentifier())
                .withValue(corsPolicy);
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
        return httpClient.createConsumer(new KongConsumer().withUsername(userUniqueName));
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
        return httpClient.createConsumerKeyAuthCredentials(id,new KongPluginKeyAuthRequest().withKey(apiKey));
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
    private <T extends KongConfigValue> void createServicePolicy(KongApi api, Policy policy, String kongIdentifier,Class<T> clazz)throws PublishingException {

        Gson gson = new Gson();
        //perform value mapping
        KongConfigValue plugin = gson.fromJson(policy.getPolicyJsonConfig(), clazz);
        KongPluginConfig config = new KongPluginConfig()
                .withName(kongIdentifier)//set required kong identifier
                .withValue(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        //execute
        config = httpClient.createPluginConfig(api.getId(),config);
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
                .withValue(plugin);
        //TODO: strong validation should be done and rollback of the service registration upon error?!
        //execute
        config = httpClient.createPluginConfig(api.getId(),config);
    }

    public static String getMetricsURI() {
        return metricsURI;
    }
}
