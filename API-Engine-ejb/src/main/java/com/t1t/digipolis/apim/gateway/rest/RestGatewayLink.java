package com.t1t.digipolis.apim.gateway.rest;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.digipolis.apim.beans.gateways.Gateway;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.*;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerAlreadyExistsException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.gateway.i18n.Messages;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.apim.kong.KongServiceBuilder;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongOAuthTokenList;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginConfig;
import com.t1t.digipolis.kong.model.KongPluginConfigList;
import com.t1t.digipolis.kong.model.KongPluginJWTResponse;
import com.t1t.digipolis.kong.model.KongPluginJWTResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthEnhanced;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.gateway.GatewayException;

import java.io.IOException;
import java.util.Map;

/**
 * An implementation of a Gateway Link that uses the Gateway's simple REST
 * API to publish Services.
 */
public class RestGatewayLink implements IGatewayLink {
    private static KongServiceBuilder kongServiceBuilder;

    static {
        kongServiceBuilder = new KongServiceBuilder();
    }

    private static final ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings("unused")
    private GatewayBean gateway;
    private KongClient httpClient;
    private String metricsURI;
    private GatewayClient gatewayClient;
    private RestGatewayConfigBean config;
    private AppConfig appConfig;
    private IStorage storage;
    private GatewayValidation gatewayValidation;

    /**
     * Constructor.
     *
     * @param gateway the gateway
     */
    public RestGatewayLink(final GatewayBean gateway, final IStorage storage, final String metricsURI, final AppConfig appConfig, final GatewayValidation gatewayValidation) {
        try {
            this.gateway = gateway;
            this.storage = storage;
            this.metricsURI = metricsURI;
            this.appConfig = appConfig;
            String cfg = gateway.getConfiguration();
            setConfig((RestGatewayConfigBean) mapper.reader(RestGatewayConfigBean.class).readValue(cfg));
            getConfig().setPassword(AesEncrypter.decrypt(getConfig().getPassword()));
            //setup http client with applicable interfaces
            httpClient = kongServiceBuilder.getService(config, KongClient.class);
            this.gatewayValidation = gatewayValidation;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KongPluginConfig createACLPlugin(Service service){
        return getClient().createACLPlugin(service);
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

    public KongConsumer createConsumerWithKongId(String kongId, String customId) throws ConsumerAlreadyExistsException{
        return getClient().createConsumerWithKongID(kongId, customId);
    }

    @Override
    public KongConsumer createConsumerWithCustomId(String customId) throws ConsumerAlreadyExistsException {
        return getClient().createConsumerWithCustomID(customId);
    }

    @Override
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id) throws ConsumerException {
        return getClient().createConsumerKeyAuth(id);
    }

    @Override
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id, String apiKey) throws ConsumerException {
        if (StringUtils.isEmpty(apiKey)) return addConsumerKeyAuth(id);
        else return getClient().createConsumerKeyAuth(id, apiKey);
    }

    @Override
    public KongPluginJWTResponse addConsumerJWT(String id,String encoding) throws ConsumerException {
        return getClient().createConsumerJWT(id,encoding);
    }

    @Override
    public KongPluginJWTResponseList getConsumerJWT(String id) throws ConsumerException {
        return getClient().getConsumerJWT(id);
    }

    @Override
    public void deleteConsumerKeyAuth(String id, String apiKey) throws ConsumerException {
        getClient().deleteConsumerKeyAuth(id, apiKey);
    }

    @Override
    public KongPluginBasicAuthResponse addConsumerBasicAuth(String userId, String userLoginName, String userLoginPassword) throws ConsumerException {
        return getClient().createConsumerBasicAuth(userId, userLoginName, userLoginPassword);
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
    public KongPluginOAuthConsumerResponse updateConsumerOAuthCredentials(String consumerId, KongPluginOAuthConsumerRequest request) {
        return getClient().updateConsumerOAuthCredentials(consumerId, request);
    }

    @Override
    public KongPluginKeyAuthResponse updateConsumerKeyAuthCredentials(String consumerId, String oldApiKey, String newApiKey) {
        return getClient().updateConsumerKeyAuthCredentials(consumerId, oldApiKey, newApiKey);
    }

    @Override
    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(String clientId) {
        return getClient().getApplicationOAuthInformation(clientId);
    }

    @Override
    public KongPluginOAuthConsumerResponseList getConsumerOAuthCredentials(String consumerId) {
        return getClient().getConsumerOAuthCredentials(consumerId);
    }

    @Override
    public void deleteOAuthConsumerPlugin(String consumerId, String pluginId) {
        getClient().deleteOAuthConsumerCredential(consumerId, pluginId);
    }

    @Override
    public KongPluginConfigList getServicePlugins(String serviceId) {
        return getClient().getServicePlugins(serviceId);
    }

    @Override
    public KongPluginConfigList getServicePlugin(String serviceId, String pluginId) {
        return getClient().getServicePlugin(serviceId, pluginId);
    }

    @Override
    public KongPluginConfig updateServicePlugin(String serviceId, KongPluginConfig config) {
        return getClient().updateServicePlugin(serviceId, config);
    }

    @Override
    public KongPluginConfig updateServicePlugin(KongPluginConfig config) {
        return getClient().updatePlugin(config);
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

    @Override
    public void updateCentralOAuthTokenExpirationTime(Integer exirationTimeInSeconds) throws PublishingException, GatewayAuthenticationException {
        getClient().updateOAuth2ExpirationForCentralOAuth(gateway, exirationTimeInSeconds);
    }

    @Override
    public Integer getCentralOAuthTokenExpirationTime() throws GatewayAuthenticationException {
        final KongPluginOAuthEnhanced oAuth2ExpirationForCentralOAuth = getClient().getOAuth2ExpirationForCentralOAuth(gateway);
        return oAuth2ExpirationForCentralOAuth.getTokenExpiration();
    }

    /**
     * @see IGatewayLink#getServiceEndpoint(String, String, String, String)
     */
    @Override
    public ServiceEndpoint getServiceEndpoint(String basePath, String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException {
        return getClient().getServiceEndpoint(basePath, organizationId, serviceId, version);
    }

    @Override
    public void registerAppConsumer(Application application, KongConsumer consumer) throws GatewayAuthenticationException {
        getClient().registerAppConsumer(application, consumer);
    }

    /**
     * @see IGatewayLink#publishService(Service)
     */
    @Override
    public Service publishService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        return getClient().publish(service);
    }

    @Override
    public void publishGatewayOAuthEndpoint(Gateway gateway) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().publishGatewayOAuthEndpoint(gateway);
    }

    @Override
    public void addGatewayOAuthScopes(String serviceId) throws PublishingException, GatewayAuthenticationException {
        KongApi api = getApi(serviceId);
        getClient().addGatewayOAuthScopes(gateway, api);
    }

    @Override
    public void removeGatewayOAuthscopes(String serviceId) throws PublishingException, GatewayAuthenticationException {
        KongApi api = getApi(serviceId);
        getClient().removeGatewayOAuthScopes(gateway, api);
    }

    /**
     * @see IGatewayLink#retireService(Service)
     */
    @Override
    public void retireService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().retire(service);
    }

    /**
     * @see IGatewayLink#registerApplication(Application)
     */
    @Override
    public Map<Contract, KongPluginConfigList> registerApplication(Application application) throws RegistrationException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new RegistrationException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        return getClient().register(application);
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
        return new GatewayClient(httpClient, gateway, storage, metricsURI, appConfig, gatewayValidation);
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

    /**
     * Adds a consumer to a service's ACL
     *
     * @param consumerId
     * @param serviceId
     * @return the KongPluginACLResponse
     */
    @Override
    public KongPluginACLResponse addConsumerToACL(String consumerId, String serviceId) {
        return getClient().addConsumerToACL(consumerId, serviceId);
    }

    /**
     * Removes a consumer's membership to a specific ACL
     *
     * @param consumerId
     * @param pluginId
     */
    @Override
    public void deleteConsumerACLPlugin(String consumerId, String pluginId) {
        getClient().deleteConsumerACLPlugin(consumerId, pluginId);
    }

    @Override
    public KongConsumerList getConsumers() {
        return getClient().getConsumers(null);
    }

    @Override
    public KongConsumerList getConsumers(String offset) {
        return getClient().getConsumers(offset);
    }

    @Override
    public KongConsumer updateOrCreateConsumer(KongConsumer consumer) {
        return getClient().upateOrCreateConsumer(consumer);
    }

    @Override
    public KongApi updateApiUpstreamURL(String organizationId, String serviceId, String version, String upstreamURL) {
        return getClient().updateApiUpstreamURL(organizationId, serviceId, version, upstreamURL);
    }

    @Override
    public String getGatewayId() {
        return gateway.getId();
    }

    @Override
    public void deleteApiPlugin(String KongApiId, String pluginId) {
        getClient().deleteApiPlugin(KongApiId, pluginId);
    }

    @Override
    public KongConsumer updateConsumer(String kongConsumerId, KongConsumer updatedConsumer) {
        return getClient().updateConsumer(kongConsumerId, updatedConsumer);
    }

    @Override
    public KongOAuthTokenList getConsumerOAuthTokenList(String consumerOAuthCredentialId, String offset) {
        return getClient().getConsumerOAuthTokenList(consumerOAuthCredentialId, offset);
    }

    @Override
    public void revokeOAuthToken(String tokenId) {
        getClient().revokeOAuthToken(tokenId);
    }

    @Override
    public KongOAuthTokenList getConsumerOAuthTokenListByUserId(String authenticatedUserId, String offset) {
        return getClient().getConsumerOAuthTokenListByUserId(authenticatedUserId, offset);
    }

    @Override
    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformationByCredentialId(String credentialId) {
        return getClient().getApplicationOAuthInformationByCredentialId(credentialId);
    }

    @Override
    public KongOAuthTokenList getOAuthToken(String tokenId) {
        return getClient().getOAuthToken(tokenId);
    }

    @Override
    public void deleteConsumerJwtCredential(String consumerId, String credentialId) {
        getClient().deleteConsumerJwtCredential(consumerId, credentialId);
    }

    @Override
    public KongPluginConfig getPlugin(String pluginId) {
        return getClient().getPlugin(pluginId);
    }

    @Override
    public Policy createServicePolicy(String organizationId, String serviceId, String version, Policy policy) {
        return getClient().createServicePolicy(organizationId, serviceId, version, policy);
    }

    @Override
    public void createServiceBranding(Service service, ServiceBrandingBean branding) {
        getClient().publishServiceBranding(service, branding.getId());
    }

    @Override
    public void deleteApi(String apiName) {
        getClient().deleteAPI(apiName);
    }

    @Override
    public KongOAuthToken getGatewayOAuthToken(String token) {
        return getClient().getGatewayOauthToken(token);
    }

    @Override
    public void revokeGatewayOAuthToken(String accessToken) {
        getClient().revokeOAuthTokenByAccessToken(accessToken);
    }

    @Override
    public KongOAuthTokenList getAllOAuth2Tokens(String offset) {
        return getClient().getAllOAuth2Tokens(offset);
    }

    @Override
    public KongOAuthToken createOAuthToken(OAuth2TokenBean token) {
        return getClient().createOAuthToken(token);
    }

    @Override
    public KongApi createApi(KongApi api) {
        return getClient().createApi(api);
    }

    @Override
    public KongApi updateOrCreateApi(KongApi api) {
        return getClient().updateApi(api);
    }

    @Override
    public KongPluginConfig createApiPlugin(String apiId, KongPluginConfig plugin) {
        return getClient().createApiPlugin(apiId, plugin);
    }

    @Override
    public KongPluginACLResponse updateConsumerACL(KongPluginACLResponse acl) {
        return getClient().updateConsumerAcl(acl);
    }

    @Override
    public KongPluginACLResponse getConsumerACL(String consumerId, String kongPluginId) {
        return getClient().getConsumerAcl(consumerId, kongPluginId);
    }

    @Override
    public KongConsumer getConsumerByCustomId(String customId) {
        return getClient().getConsumerByCustomId(customId);
    }

    @Override
    public KongPluginConfig updatePlugin(KongPluginConfig plugin) {
        return getClient().updatePlugin(plugin);
    }

    @Override
    public KongPluginConfigList getConsumerPlugins(String consumerId) {
        return getClient().getConsumerPlugins(consumerId);
    }

    @Override
    public KongPluginACLResponseList getAllConsumerAcls(String consumerId) {
        return getClient().getAllConsumerAcls(consumerId);
    }

    @Override
    public KongPluginConfigList getConsumerSpecificApiPlugins(String consumerId, String apiId) {
        return getClient().getConsumerSpecificApiPlugins(consumerId, apiId);
    }
}
