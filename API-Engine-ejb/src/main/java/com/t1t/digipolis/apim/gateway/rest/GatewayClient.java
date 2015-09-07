package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.exceptions.ActionException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.kong.model.*;
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
        //todo set basepath
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
                .withUsername((application.getOrganizationId()+"."+application.getApplicationId() + "." + application.getVersion()).toLowerCase())
                .withCustomId((application.getOrganizationId() + "."+application.getApplicationId()+"."+application.getVersion()).toLowerCase());//conventionally lower case
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
            api = new KongApi().withName(generateServiceUniqueName(contract.getServiceOrgId(),contract.getServiceId(),contract.getServiceVersion()));
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

        //register additional policies


        //what about other authorization policies? actions on different endpoint?

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
        String nameAndDNS = generateServiceUniqueName(service);
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
            api = httpClient.addApi(api);
        }catch (RetrofitError error){
            //start new client to comm with kong
            throw new ActionException(error.getMessage());
        }

        //flag for custom CORS policy
        boolean customCorsFlag = false;
        //flag for custom KeyAuth policy
        boolean customKeyAuth = false;
        //verify if api creation has been succesfull
        if(!StringUtils.isEmpty(api.getId())){
            List<Policy> policyList = service.getServicePolicies();
            for(Policy policy:policyList){
                //execute policy
                Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                switch(policies){
                    //all policies can be available here
                    case BASICAUTHENTICATION: createServicePolicy(api, policy, Policies.BASICAUTHENTICATION.getKongIdentifier(),Policies.BASICAUTHENTICATION.getClazz());break;
                    case CORS: createServicePolicy(api, policy, Policies.CORS.getKongIdentifier(),Policies.CORS.getClazz());customCorsFlag=true;break;
                    case FILELOG: createServicePolicy(api, policy, Policies.FILELOG.getKongIdentifier(),Policies.FILELOG.getClazz());break;
                    case HTTPLOG: createServicePolicy(api, policy, Policies.HTTPLOG.getKongIdentifier(),Policies.HTTPLOG.getClazz());break;
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
                    default:break;
                }
            }
        }

        //add default CORS Policy if no custom CORS defined
        if(!customCorsFlag) registerDefaultCORSPolicy(api);
        if(!customKeyAuth) registerDefaultKeyAuthPolicy(api);
    }

    /**
     * Registers the default keyauth plugin with apikey key_value (service-scoped policy).
     * Only consumers having an valid API key can access the API.
     * TODO Kong 0.5.0 - add ACL group
     * @param api
     */
    private void registerDefaultKeyAuthPolicy(KongApi api) {
        KongPluginKeyAuth keyAuthPolicy = new KongPluginKeyAuth()
                .withKeyNames(Arrays.asList("apikey"));
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
        KongPluginCors corsPolicy = new KongPluginCors(); //default values are ok
        KongPluginConfig config = new KongPluginConfig()
                .withName(Policies.CORS.getKongIdentifier())
                .withValue(corsPolicy);
        httpClient.createPluginConfig(api.getId(),config);
    }

    /**
     * Generates a unique service name for Kong.
     * The name is constituted of the organizationid.serviceid.version
     *
     * @param service
     * @return
     */
    private String generateServiceUniqueName(Service service) {
        return generateServiceUniqueName(service.getOrganizationId(), service.getServiceId(), service.getVersion());
    }

    private String generateServiceUniqueName(String orgId, String serviceId, String serviceVersionsId){
        StringBuilder serviceGatewayName = new StringBuilder(orgId)
                .append(".")
                .append(serviceId)
                .append(".")
                .append(serviceVersionsId);
        return serviceGatewayName.toString().toLowerCase();
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
        String nameAndDNS = generateServiceUniqueName(organizationId,serviceId,version);
        httpClient.deleteApi(nameAndDNS);
    }

    public KongConsumer getConsumer(String id){
        return httpClient.getConsumer(id);
    }

    public KongConsumer createConsumer(String customId){
        return httpClient.createConsumer(new KongConsumer().withCustomId(customId));
    }

    public KongPluginKeyAuthResponseList getConsumerKeyAuth(String id){
        httpClient.getConsumerKeyAuthCredentials(id);
        return null;
    }

    public KongPluginKeyAuthResponse createConsumerKeyAuth(String id){
        return httpClient.createConsumerKeyAuthCredentials(id,new KongPluginKeyAuthRequest());
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
}
