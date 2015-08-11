package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;

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
     * @param organizationId the org id
     * @param serviceId the service id
     * @param version the version
     * @return the service endpoint
     * @throws GatewayAuthenticationException when unable to authenticate with gateway
     */
    public ServiceEndpoint getServiceEndpoint(String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException;

    /**
     * Close down the gateway link when it's no longer needed.
     */
    public void close();
    
}