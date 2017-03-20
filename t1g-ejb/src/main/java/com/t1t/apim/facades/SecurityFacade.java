package com.t1t.apim.facades;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.apps.NewApiKeyBean;
import com.t1t.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ApplicationNotFoundException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.JWTException;
import com.t1t.apim.exceptions.OAuthException;
import com.t1t.apim.gateway.GatewayAuthenticationException;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.apim.security.JWTExpTimeResponse;
import com.t1t.apim.security.OAuthExpTimeResponse;
import com.t1t.util.ConsumerConventionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by michallispashidis on 10/04/16.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SecurityFacade {
    private static final Logger _LOG = LoggerFactory.getLogger(SecurityFacade.class.getName());
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;
    @Inject private AppConfig config;
    @Inject private GatewayFacade gatewayFacade;
    @Inject private OrganizationFacade orgFacade;

    public void setOAuthExpTime(Integer expTime){
        try {
            if (!config.getOAuthEnableGatewayEnpoints())
            {
                List<GatewayBean> gateways = query.getAllGateways();
                for (GatewayBean gw : gateways) {
                    gw.setOAuthExpTime(expTime);
                    storage.updateGateway(gw);
                }
            }
            else {
                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                gateway.updateCentralOAuthTokenExpirationTime(expTime);
            }
        }
        catch (StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
        catch (GatewayAuthenticationException e) {
            throw new OAuthException("Could not update the OAuth expiration time"+e.getMessage());
        }
    }

    public OAuthExpTimeResponse getOAuthExpTime(){
        try {
            OAuthExpTimeResponse oAuthExpTimeResponse = new OAuthExpTimeResponse();
            if (!config.getOAuthEnableGatewayEnpoints()) {
                oAuthExpTimeResponse.setExpirationTime(query.getDefaultGateway().getOAuthExpTime());
            }
            else {
                //We create the new application version consumer
                IGatewayLink gateway = gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
                final Integer centralOAuthTokenExpirationTime = gateway.getCentralOAuthTokenExpirationTime();
                oAuthExpTimeResponse.setExpirationTime(centralOAuthTokenExpirationTime);
            }
            return oAuthExpTimeResponse;
        }
        catch (StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
        catch (GatewayAuthenticationException e) {
            throw new OAuthException("Could not update the OAuth expiration time"+e.getMessage());
        }
    }

    public void setJWTExpTime(Integer expTime){
        try {
            UpdateGatewayBean updateGatewayBean = new UpdateGatewayBean();
            updateGatewayBean.setJwtExpTime(expTime);
            gatewayFacade.update(gatewayFacade.getDefaultGateway().getId(),updateGatewayBean);
        } catch (StorageException e) {
            throw new JWTException("Could not update the JWT expiration time:"+e.getMessage());
        }
    }

    public JWTExpTimeResponse getJWTExpTime(){
        try {
            Integer exptime = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId()).getJWTExpTime();
            JWTExpTimeResponse response = new JWTExpTimeResponse();
            response.setExpirationTime(exptime);
            return response;
        } catch (StorageException e) {
            throw new JWTException("Could not return the JWT expiration time:"+e.getMessage());
        }
    }

    public Set<NewApiKeyBean> reissueAllApiKeys() {
        Set<NewApiKeyBean> rval = new HashSet<>();
        for (ApplicationVersionBean avb : getAllNonRetiredApplicationVersions()) {
            try {
                NewApiKeyBean nakb = orgFacade.reissueApplicationVersionApiKey(avb);
                if (nakb != null) {
                    rval.add(nakb);
                }
            }
            catch (Exception ex) {
                //Log the error, but continue the reissuance process
                _LOG.error("Key Auth Reissuance FAILED for {}, caused by:{}", ConsumerConventionUtil.createAppUniqueId(avb), ex);
            }
        }
        return rval;
    }

    public Set<NewOAuthCredentialsBean> reissueAllOAuthCredentials() {
        Set<NewOAuthCredentialsBean> rval = new HashSet<>();
        for (ApplicationVersionBean avb : getAllNonRetiredApplicationVersions()) {
            try {
                NewOAuthCredentialsBean nocb = orgFacade.reissueApplicationVersionOAuthCredentials(avb);
                if (nocb != null) {
                    rval.add(nocb);
                }
            }
            catch (Exception ex) {
                //Log the error, but continue the reissuance process
                _LOG.error("OAuth2 Credentials Reissuance FAILED for {}, caused by:{}", ConsumerConventionUtil.createAppUniqueId(avb), ex);
            }
        }
        return rval;
    }

    private List<ApplicationVersionBean> getAllNonRetiredApplicationVersions() {
        try {
            return query.getAllNonRetiredApplicationVersions();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void revokeOAuthToken(String accessToken) {
        try {
            query.getAllGateways().stream()
                    .map(GatewayBean::getId)
                    .map(gatewayFacade::createGatewayLink)
                    .forEach(gw -> gw.revokeGatewayOAuthToken(accessToken));
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }
}
