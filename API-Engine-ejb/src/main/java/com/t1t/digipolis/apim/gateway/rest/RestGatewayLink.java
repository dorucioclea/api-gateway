package com.t1t.digipolis.apim.gateway.rest;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerAlreadyExistsException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.gateway.i18n.Messages;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.apim.kong.RestServiceBuilder;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.gateway.GatewayException;

import java.io.IOException;

/**
 * An implementation of a Gateway Link that uses the Gateway's simple REST
 * API to publish Services.
 */
public class RestGatewayLink implements IGatewayLink {
    private static RestServiceBuilder restServiceBuilder;

    static {
        restServiceBuilder = new RestServiceBuilder();
    }

    private static final ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings("unused")
    private GatewayBean gateway;
    private KongClient httpClient;
    private GatewayClient gatewayClient;
    private RestGatewayConfigBean config;
    private IStorage storage;

    /**
     * Constructor.
     *
     * @param gateway the gateway
     */
    public RestGatewayLink(final GatewayBean gateway, final IStorage storage) {
        try {
            this.gateway = gateway;
            this.storage = storage;
            String cfg = gateway.getConfiguration();
            setConfig((RestGatewayConfigBean) mapper.reader(RestGatewayConfigBean.class).readValue(cfg));
            getConfig().setPassword(AesEncrypter.decrypt(getConfig().getPassword()));
            //setup http client with applicable interfaces
            httpClient = restServiceBuilder.getService(config, KongClient.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see IGatewayLink#close()
     */
    @Override
    public void close() {
        //deprecated, handled by the retrofit client
    }

    @Override
    public KongConsumer getConsumer(String id) throws ConsumerException {
        return getClient().getConsumer(id);
    }

    @Override
    public void deleteConsumer(String id) throws ConsumerException {
        getClient().deleteConsumer(id);
    }

    @Override
    public KongPluginKeyAuthResponseList getConsumerKeyAuth(String id) throws ConsumerException {
        return getClient().getConsumerKeyAuth(id);
    }

    @Override
    public KongConsumer createConsumer(String id) throws ConsumerAlreadyExistsException {
        return getClient().createConsumer(id);
    }

    @Override
    public KongConsumer createConsumer(String userId, String customId) throws ConsumerAlreadyExistsException {
        return getClient().createConsumer(userId, customId);
    }

    @Override
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id) throws ConsumerException {
        return getClient().createConsumerKeyAuth(id);
    }

    @Override
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id, String apiKey) throws ConsumerException {
        if(StringUtils.isEmpty(apiKey))return addConsumerKeyAuth(id);
        else return getClient().createConsumerKeyAuth(id, apiKey);
    }

    @Override
    public void deleteConsumerKeyAuth(String id, String apiKey)throws ConsumerException{
        getClient().deleteConsumerKeyAuth(id,apiKey);
    }

    @Override
    public KongPluginBasicAuthResponse addConsumerBasicAuth(String userId, String userLoginName,String userLoginPassword) throws ConsumerException {
        return getClient().createConsumerBasicAuth(userId,userLoginName,userLoginPassword);
    }

    @Override
    public KongPluginBasicAuthResponseList getConsumerBasicAuth(String id) throws ConsumerException {
        return getClient().getConsumerBasicAuth(id);
    }

    @Override
    public com.t1t.digipolis.kong.model.KongApi getApi(String id) throws GatewayException {
        return getClient().getApi(id);
    }

    @Override
    public KongPluginOAuthConsumerResponse enableConsumerForOAuth(String consumerId, KongPluginOAuthConsumerRequest request) {
        return getClient().enableConsumerForOAuth(consumerId, request);
    }

    @Override
    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(String clientId) {
        return getClient().getApplicationOAuthInformation(clientId);
    }

    /**
     * Checks that the gateway is up.
     */
    private boolean isGatewayUp() throws GatewayAuthenticationException {
        SystemStatus status = getClient().getStatus();
        return status.isUp();
    }

    /**
     * @see IGatewayLink#getStatus()
     */
    @Override
    public SystemStatus getStatus() throws GatewayAuthenticationException {
        return getClient().getStatus();
    }

    /**
     * @see IGatewayLink#getServiceEndpoint(String, String, String, String)
     */
    @Override
    public ServiceEndpoint getServiceEndpoint(String basePath,String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException {
        return getClient().getServiceEndpoint(basePath,organizationId, serviceId, version);
    }

    @Override
    public void registerAppConsumer(Application application, KongConsumer consumer) throws GatewayAuthenticationException {
        getClient().registerAppConsumer(application, consumer);
    }

    /**
     * @see IGatewayLink#publishService(Service)
     */
    @Override
    public void publishService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().publish(service);
    }

    /**
     * @see IGatewayLink#retireService(Service)
     */
    @Override
    public void retireService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().retire(service.getOrganizationId(), service.getServiceId(), service.getVersion());
    }

    /**
     * @see IGatewayLink#registerApplication(Application)
     */
    @Override
    public void registerApplication(Application application) throws RegistrationException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new RegistrationException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().register(application);
    }

    /**
     * @see IGatewayLink#unregisterApplication(Application)
     */
    @Override
    public void unregisterApplication(Application application) throws RegistrationException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new RegistrationException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().unregister(application.getOrganizationId(), application.getApplicationId(), application.getVersion());
    }

    /**
     * @return the gateway client
     */
    protected GatewayClient getClient() {
        if (gatewayClient == null) {
            gatewayClient = createClient();
        }
        return gatewayClient;
    }

    /**
     * @return a newly created rest gateway client
     */
    private GatewayClient createClient() {
        String gatewayEndpoint = getConfig().getEndpoint();
        return new GatewayClient(httpClient,gateway,storage);
    }

    /**
     * @return the config
     */
    public RestGatewayConfigBean getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(RestGatewayConfigBean config) {
        this.config = config;
    }

}
