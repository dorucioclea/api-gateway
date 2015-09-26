package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerAlreadyExistsException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.ConsumerException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginBasicAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponse;
import com.t1t.digipolis.kong.model.KongPluginKeyAuthResponseList;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import org.elasticsearch.gateway.GatewayException;

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
     * Publishes a new {@link Service}.
     * @param service the service being published
     * @throws PublishingException when unable to publish service
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     */
    public void publishService(Service service) throws PublishingException, GatewayAuthenticationException;

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
     * @throws RegistrationException when unable to register application
     * @throws GatewayAuthenticationException when unable to authenticate with gateway  
     * @throws PublishingException when unable to publish application
     */
    public void registerApplication(Application application) throws RegistrationException, GatewayAuthenticationException;

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
    public ServiceEndpoint getServiceEndpoint(String basePath, String organizationId, String serviceId, String version)
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
     * Create a new consumer
     *
     * @param userId    unique username used by Kong
     * @param customId  customId for the API service to use
     * @return
     * @throws ConsumerAlreadyExistsException
     */
    public KongConsumer createConsumer(String userId, String customId) throws ConsumerAlreadyExistsException;

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
    public com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse enableConsumerForOAuth(String consumerId,KongPluginOAuthConsumerRequest request);

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
    
}
