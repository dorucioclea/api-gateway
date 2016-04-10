package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;
import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.apim.security.OAuthExpTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 10/04/16.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SecurityFacade {
    private static Logger _LOG = LoggerFactory.getLogger(SecurityFacade.class.getName());
    @Inject private ISecurityContext securityContext;
    @Inject private ISecurityAppContext appContext;
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private AppConfig config;
    @Inject private GatewayFacade gatewayFacade;

    public void setOAuthExpTime(Integer expTime){
        if(!config.getOAuthEnableGatewayEnpoints())throw new OAuthException("Central OAuth2 endpoints are deactivate, this method cannot be used in the current configruation.");
        else{
            try {
                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                gateway.updateCentralOAuthTokenExpirationTime(expTime);
            } catch (StorageException e) {
                throw new ApplicationNotFoundException(e.getMessage());
            } catch (GatewayAuthenticationException e) {
                throw new OAuthException("Could not update the OAuth expiration time"+e.getMessage());
            }
        }
    }

    public OAuthExpTimeResponse getOAuthExpTime(){
        if(!config.getOAuthEnableGatewayEnpoints())throw new OAuthException("Central OAuth2 endpoints are deactivate, this method cannot be used in the current configruation.");
        else{
            try {
                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                final Integer centralOAuthTokenExpirationTime = gateway.getCentralOAuthTokenExpirationTime();
                OAuthExpTimeResponse oAuthExpTimeResponse = new OAuthExpTimeResponse();
                oAuthExpTimeResponse.setExpirationTime(centralOAuthTokenExpirationTime);
                return oAuthExpTimeResponse;
            } catch (StorageException e) {
                throw new ApplicationNotFoundException(e.getMessage());
            } catch (GatewayAuthenticationException e) {
                throw new OAuthException("Could not update the OAuth expiration time"+e.getMessage());
            }
        }
    }
}
