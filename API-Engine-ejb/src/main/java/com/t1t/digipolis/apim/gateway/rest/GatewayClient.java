package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.kong.model.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jgroups.protocols.RATE_LIMITER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * A REST client for accessing the Gateway API.
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

    public void register(Application application) throws RegistrationException, GatewayAuthenticationException {
        //register API
        //register consumer application
        //register policies?

    }

    public void unregister(String organizationId, String applicationId, String version) throws RegistrationException, GatewayAuthenticationException {
        //get all applicable policies
        //remove policies
    }

    /**
     * Publishes an API to the kong gateway.
     * The properties path and target_url are variable, and will be retrieved from the service dto.
     * The properties strip_path, preserve_host and public_dns will be set equally for all registered endpoints
     * The path should be: /organizationname/applicationname/version
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
        api.setPublicDns(Policies.CORS.getKongIdentifier());
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
        api = httpClient.addApi(api);
        //verify if api creation has been succesfull
        if(!StringUtils.isEmpty(api.getId())){
            List<Policy> policyList = service.getServicePolicies();
            for(Policy policy:policyList){
                //execute policy
                Policies policies = Policies.valueOf(policy.getPolicyImpl().toUpperCase());
                switch(policies){
                    case BASICAUTHENTICATION: createServicePolicy(api, policy, Policies.BASICAUTHENTICATION.getKongIdentifier(),Policies.BASICAUTHENTICATION.getClazz());break;
                    case CORS: createServicePolicy(api, policy, Policies.CORS.getKongIdentifier(),Policies.CORS.getClazz());break;
                    case FILELOG: createServicePolicy(api, policy, Policies.FILELOG.getKongIdentifier(),Policies.FILELOG.getClazz());break;
                    case HTTPLOG: createServicePolicy(api, policy, Policies.HTTPLOG.getKongIdentifier(),Policies.HTTPLOG.getClazz());break;
                    case UDPLOG: createServicePolicy(api, policy, Policies.UDPLOG.getKongIdentifier(),Policies.UDPLOG.getClazz());break;
                    case TCPLOG: createServicePolicy(api, policy, Policies.TCPLOG.getKongIdentifier(),Policies.TCPLOG.getClazz());break;
                    case IPRESTRICTION: createServicePolicy(api, policy, Policies.IPRESTRICTION.getKongIdentifier(),Policies.IPRESTRICTION.getClazz());break;
                    case KEYAUTHENTICATION: createServicePolicy(api, policy, Policies.KEYAUTHENTICATION.getKongIdentifier(),Policies.KEYAUTHENTICATION.getClazz());break;
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
    }



    /**
     * Generates a unique service name for Kong.
     * The name is constituted of the organizationid.serviceid.version
     *
     * @param service
     * @return
     */
    private String generateServiceUniqueName(Service service) {
        StringBuilder serviceGatewayName = new StringBuilder(service.getOrganizationId())
                .append(".")
                .append(service.getServiceId())
                .append(".")
                .append(service.getVersion());
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

    public void retire(String organizationId, String serviceId, String version) throws RegistrationException, GatewayAuthenticationException {

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
        //TODO: how to validate or rollback?!
        //execute
        config = httpClient.createPluginConfig(api.getId(),config);
    }
}
