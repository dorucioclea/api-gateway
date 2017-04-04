package com.t1t.apim.gateway;

import com.t1t.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.gateways.Gateway;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.gateway.dto.*;
import com.t1t.apim.gateway.dto.exceptions.ConsumerAlreadyExistsException;
import com.t1t.apim.gateway.dto.exceptions.ConsumerException;
import com.t1t.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.kong.model.*;
import org.elasticsearch.gateway.GatewayException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Links the design time API with a Gateway.  This allows the design time API
 * to interface with the runtime Gateway in order to do things like publishing
 * Services and Contracts.
 */
public interface IGatewayLink {

    /**
     * Gets the current status of the gateway.
     * @return the system status
     * @throws GatewayAuthenticationException when unable to authenticate with gateway 
     */
    public SystemStatus getStatus() throws GatewayAuthenticationException;

    /**
     * Sets the OAuth2 expiration time for tokens issues on the gateway's central oauth endpoints.
     *
     * @param exirationTimeInSeconds
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public void updateCentralOAuthTokenExpirationTime(Integer exirationTimeInSeconds) throws PublishingException, GatewayAuthenticationException;

    /**
     * Creates an ACL plugin for given service.
     *
     * @param service
     * @return
     */
    public KongPluginConfig createACLPlugin(Service service);

    /**
     * Returns the central OAuth expiration time value.
     *
     * @return
     * @throws GatewayAuthenticationException
     */
    public Integer getCentralOAuthTokenExpirationTime() throws GatewayAuthenticationException;

    /**
     * Publishes a new {@link Service}.
     * @param service the service being published
     * @throws PublishingException when unable to publish service
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     */
    public Service publishService(Service service) throws PublishingException, GatewayAuthenticationException;

    /**
     * Published a centralized OAuth authorization and token endpoint for a gateway.
     *
     * @param gateway
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public void publishGatewayOAuthEndpoint(Gateway gateway)throws PublishingException, GatewayAuthenticationException;

    /**
     * Add the OAuth scopes enabled on the api to the centralized OAuth endpoints.
     *
     * @param serviceId
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public void addGatewayOAuthScopes(String serviceId)throws PublishingException, GatewayAuthenticationException;

    /**
     * Removes the OAuth scopes from the given api (oauth policies) on the central oauth api
     * @param serviceId
     * @throws PublishingException
     * @throws GatewayAuthenticationException
     */
    public void removeGatewayOAuthscopes(String serviceId) throws PublishingException, GatewayAuthenticationException;

    /**
     * Retires (removes) a {@link Service} from the registry.
     * @param service the service to retire/remove
     * @throws PublishingException when unable to retire service
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     */
    public void retireService(Service service) throws PublishingException, GatewayAuthenticationException;
    
    /**
     * Registers a new {@link Application}.  An application is ultimately a collection of
     * contracts to managed services.
     * @param application the application being registered
     * @return a map with a contract as key and the plugin configs for that contract as value
     * @throws RegistrationException when unable to register application
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     * @throws PublishingException when unable to publish application
     */
    public Map<Contract, KongPluginConfigList> registerApplication(Application application) throws RegistrationException, GatewayAuthenticationException;

    /**
     * Removes an {@link Application} from the registry.
     * @param application the application to remove
     * @throws RegistrationException when unable to register
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     */
    public void unregisterApplication(Application application) throws RegistrationException, GatewayAuthenticationException;

    /**
     * Gets the service endpoint from the gateway.
     * @param basePath the base path declared in the service definition
     * @param organizationId the org id
     * @param serviceId the service id
     * @param version the version
     * @return the service endpoint
     * @throws GatewayAuthenticationException when unable to authenticate with gateway
     */
    public ServiceEndpoint getServiceEndpoint(Set<String> basePaths, String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException;

    /**
     * Registers an appConsumer for all policies, and for each service, in the context of an application.
     * The result is that all available service policies are enforced for the given appConsumer, when accessing the API services through the gateway.
     *
     * @param application
     * @param consumer
     * @throws GatewayAuthenticationException
     */
    public void registerAppConsumer(Application application, KongConsumer consumer)throws GatewayAuthenticationException;

    /**
     * Close down the gateway link when it's no longer needed.
     */
    public void close();

    /**
     * Returns the consumer object for given consumer id.
     *
     * @param id
     * @return
     * @throws ConsumerException
     */
    public KongConsumer getConsumer(String id) throws ConsumerException;

    /**
     * Deletes an end-user consumer (in the context of an application).
     * The convential name == application context.
     *
     * @param id
     * @throws ConsumerException
     */
    public void deleteConsumer(String id)throws ConsumerException;

    /**
     * Retrieve a consumer information with it's API key.
     *
     * @param id
     * @return
     * @throws ConsumerException
     */
    public KongPluginKeyAuthResponseList getConsumerKeyAuth(String id) throws ConsumerException;

    /**
     * Creates a new consumer.
     *
     * @param userId    unique username used by Kong
     * @return
     * @throws ConsumerException
     */
    public KongConsumer createConsumer(String userId) throws ConsumerAlreadyExistsException;

    /**
     * Create a real user/consumer based on its customId - we can not use easily user unique id's in the username field
     * so, we let Kong generate them for us.
     *
     * @param customId
     * @return
     * @throws ConsumerAlreadyExistsException
     */
    public KongConsumer createConsumerWithCustomId(String customId)throws ConsumerAlreadyExistsException;

    /**
     * Create a new consumer
     *
     * @param userId    unique username used by Kong
     * @param customId  customId for the API service to use
     * @return
     * @throws ConsumerAlreadyExistsException
     */
    public KongConsumer createConsumer(String userId, String customId) throws ConsumerAlreadyExistsException;

    /**
     * Create a new consumer, with given Kong id (Kong will not generate Id; you must be sure that the id is unique.
     *
     * @param kongId
     * @param customId
     * @return
     * @throws ConsumerAlreadyExistsException
     */
    public KongConsumer createConsumerWithKongId(String kongId, String customId) throws ConsumerAlreadyExistsException;

    /**
     * Adds key auth to a consumer, generating a new API Key.
     * @param id
     * @return
     * @throws ConsumerException
     */
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id) throws ConsumerException;

    /**
     * Add key auth to a consumer with given API Key.
     * @param id
     * @param apiKey
     * @return
     * @throws ConsumerException
     */
    public KongPluginKeyAuthResponse addConsumerKeyAuth(String id,String apiKey) throws ConsumerException;

    /**
     * Adds JWT to a consumer, generating a new key/secret on the API Engine.
     * @param consumerName
     * @param publicRsaKey
     * @return
     * @throws ConsumerException
     */
    public KongPluginJWTResponse addConsumerJWT(String consumerName, String publicRsaKey) throws ConsumerException;

    /**
     * Retrieve a consumer information with it's JWT key.
     *
     * @param id
     * @return
     * @throws ConsumerException
     */
    public KongPluginJWTResponseList getConsumerJWT(String id) throws ConsumerException;

    /**
     * Deletes a key auth credential for a specific consumer.
     * This should be done when removing a contract from an application.
     *
     * @param id
     * @param apiKey
     * @throws ConsumerException
     */
    public void deleteConsumerKeyAuth(String id, String apiKey)throws ConsumerException;

    /**
     * Adds basic authentication with a username and password
     * @param userId
     * @param userLoginName
     * @param userLoginPassword
     * @return
     * @throws ConsumerException
     */
    public KongPluginBasicAuthResponse addConsumerBasicAuth(String userId, String userLoginName, String userLoginPassword) throws ConsumerException;

    /**
     * Returns a list of basic credentials for a given user
     * @param id
     * @return
     * @throws ConsumerException
     */
    public KongPluginBasicAuthResponseList getConsumerBasicAuth(String id) throws ConsumerException;

    /**
     * Returns the kong api based on its conventional/generated id.
     *
     * @param id
     * @return
     * @throws GatewayException
     */
    public KongApi getApi(String id) throws GatewayException;

    /**
     * Enable a consumer for OAuth2.
     *
     * @param consumerId
     * @param request
     * @return
     */
    public KongPluginOAuthConsumerResponse enableConsumerForOAuth(String consumerId, KongPluginOAuthConsumerRequest request);

    /**
     * Update a consumer's key auth credentials
     *
     * @param consumerId
     * @param oldApiKey
     * @param newApiKey
     * @return
     */
    public KongPluginKeyAuthResponse updateConsumerKeyAuthCredentials(String consumerId, String oldApiKey, String newApiKey);

    /**
     * Update OAuth credentials for consumer
     *
     * @param consumerId
     * @param request
     * @return
     */
    public KongPluginOAuthConsumerResponse updateConsumerOAuthCredentials(String consumerId, KongPluginOAuthConsumerRequest request);

    /**
     * Get application specific information for OAuth.
     *
     * @param clientId
     * @return
     */
    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformation(String clientId);

    /**
     * Get a list of all oauth credentials associated with a user.
     *
     * @param consumerId
     * @return
     */
    public KongPluginOAuthConsumerResponseList getConsumerOAuthCredentials(String consumerId);

    /**
     * Delete a created OAuth credential for a given consumer.
     *
     * @param consumerId
     * @param pluginId
     */
    public void deleteOAuthConsumerPlugin(String consumerId, String pluginId);

    /**
     * Returns all plugins for a given service.
     *
     * @param serviceId
     * @return
     */
    public KongPluginConfigList getServicePlugins(String serviceId);

    /**
     * Returns a plugin for a service by its unique id.
     *
     * @param serviceId
     * @param pluginId
     * @return
     */
    public KongPluginConfigList getServicePlugin(String serviceId, String pluginId);

    /**
     * Updates a given plugin for a service.
     *
     * @param serviceId
     * @param config
     * @return
     */
    public KongPluginConfig updateServicePlugin(String serviceId, KongPluginConfig config);

    /**
     * Updates a plugin on the gateway
     * @param config
     * @return
     */
    public KongPluginConfig updateServicePlugin(KongPluginConfig config);

    /**
     * Adds a consumer to a service ACL
     *
     * @param consumerId
     * @param serviceId
     * @return
     */
    public KongPluginACLResponse addConsumerToACL(String consumerId, String serviceId);

    /**
     * Remove a consumer from a service's ACL
     * @param consumerId
     * @param pluginId
     */
    public void deleteConsumerACLPlugin(String consumerId, String pluginId);

    /**
     * Get a list of consumers
     * @return KongConsumerList
     */
    public KongConsumerList getConsumers();

    /**
     * Get a list of consumers
     * @param offset
     * @return
     */
    public KongConsumerList getConsumers(String offset);

    /**
     * Update or create a consumer
     * @param consumer
     * @return
     */
    public KongConsumer updateOrCreateConsumer(KongConsumer consumer);

    /**
     * update an API's upstream URL
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @param upstreamURL
     * @return KongApi
     */
    public KongApi updateServiceVersionOnGateway(ServiceVersionBean svb);

    /**
     * @return the gateway link's id
     */
    public String getGatewayId();

    /**
     * Delete a service plugin
     * @param KongApiId
     * @param pluginId
     */
    public void deleteApiPlugin(String KongApiId, String pluginId);

    /**
     * Update a consumer on the gateway
     * @param kongConsumerId
     * @return
     */
    public KongConsumer updateConsumer(String kongConsumerId, KongConsumer updatedConsumer);

    /**
     * Retrieve a consumer's OAuth2 tokens
     * @param consumerOAuthCredentialId
     * @param offset base64 encoded page number
     * @return
     */
    public KongOAuthTokenList getConsumerOAuthTokenList(String consumerOAuthCredentialId, String offset);

    /**
     * Delete a token on the gateway corresponding to the id
     * @param tokenId
     */
    public void revokeOAuthToken(String tokenId);

    /**
     * Retrieve oauth tokens by authenticated user id
     * @param authenticatedUserId
     * @param offset base64 encoded page number
     * @return
     */
    public KongOAuthTokenList getConsumerOAuthTokenListByUserId(String authenticatedUserId, String offset);

    /**
     * Retrieve OAuth information based on credential id
     * @param credentialId
     * @return
     */
    public KongPluginOAuthConsumerResponseList getApplicationOAuthInformationByCredentialId(String credentialId);

    /**
     * Retrieve an OAuth token from the gateway
     * @param tokenId
     * @return
     */
    public KongOAuthTokenList getOAuthToken(String tokenId);

    /**
     * Delete a consumer's JWT credential
     * @param consumerId
     * @param credentialId
     */
    public void deleteConsumerJwtCredential(String consumerId, String credentialId);

    /**
     * Return a service plugin
     * @param pluginId
     * @return
     */
    public KongPluginConfig getPlugin(String pluginId);

    /**
     * Creates a service policy/plugin on the gateway
     * @param organizationId
     * @param serviceId
     * @param version
     * @param policy
     * @return
     */
    public Policy createServicePolicy(String organizationId, String serviceId, String
            version, Policy policy);

    /**
     * Create a service branding api on the gateway
     * @param service
     */
    public void createServiceBranding(Service service, ServiceBrandingBean branding);

    /**
     * Delete an API on the gateway
     * @param apiName
     */
    public void deleteApi(String apiName);

    /**
     * Get the gateway OAuth token corresponding to an access token
     * @param token
     * @return
     */
    public KongOAuthToken getGatewayOAuthToken(String token);

    /**
     * Revoke an OAuth token on the gateway corresponding to the access token
     * @param accessToken
     */
    public void revokeGatewayOAuthToken(String accessToken);

    /**
     * Retrieve all oauth tokens on the gateway
     * @param offset
     */
    public KongOAuthTokenList getAllOAuth2Tokens(String offset);

    /**
     * Create a token on the gateway
     * @param token
     * @return
     */
    public KongOAuthToken createOAuthToken(OAuth2TokenBean token);

    /**
     * Create an api on the gateway
     * @param api
     * @return
     */
    public KongApi createApi(KongApi api);

    /**
     * Create or update the api on the gateway
     * @param api
     * @return
     */
    public KongApi updateOrCreateApi(KongApi api);

    /**
     * Create an api plugin on the gateway
     * @param apiId
     * @param plugin
     * @return
     */
    public KongPluginConfig createApiPlugin(String apiId, KongPluginConfig plugin);

    /**
     * Update an consumer acl plugin on the gateway
     * @param acl
     * @return
     */
    public KongPluginACLResponse updateConsumerACL(KongPluginACLResponse acl);

    /**
     * Retrieve a consumer's acl plugin
     * @param consumerId
     * @param kongPluginId
     * @return
     */
    public KongPluginACLResponse getConsumerACL(String consumerId, String kongPluginId);

    /**
     * Return a consumer based on it's custom id. Returns null if no consumer is found, returns the consumer with the latest
     * creation date if multiple are present
     * @param customId
     * @return
     */
    public KongConsumer getConsumerByCustomId(String customId);

    /**
     * Update a plugin on the gateway
     * @param plugin
     * @return
     */
    public KongPluginConfig updatePlugin(KongPluginConfig plugin);

    /**
     * Retrieves plugins for a given consumer id
     * @param consumerId
     * @return
     */
    public KongPluginConfigList getConsumerPlugins(String consumerId);

    /**
     * Retrieves all consumer acl groups
     * @param consumerId
     * @return
     */
    public KongPluginACLResponseList getAllConsumerAcls(String consumerId);

    /**
     * Retrieve consumer-specific api plugin
     * @param consumerId
     * @param apiId
     * @return
     */
    public KongPluginConfigList getConsumerSpecificApiPlugins(String consumerId, String apiId);
}
