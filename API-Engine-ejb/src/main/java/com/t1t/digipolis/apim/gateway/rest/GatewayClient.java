package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

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
        api.setPublicDns("");
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
        httpClient.addApi(api);
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
}
