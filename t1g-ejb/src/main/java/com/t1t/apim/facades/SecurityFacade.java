package com.t1t.apim.facades;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.apps.NewApiKeyBean;
import com.t1t.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.UpdateGatewayBean;
import com.t1t.apim.beans.idm.IdpIssuerBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ApplicationNotFoundException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.JwtException;
import com.t1t.apim.security.ISecurityAppContext;
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
    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private OrganizationFacade orgFacade;
    @Inject
    private ISecurityAppContext appCtx;

    public OAuthExpTimeResponse getOAuthExpTime() {
        try {
            final OAuthExpTimeResponse oAuthExpTimeResponse = new OAuthExpTimeResponse();
            oAuthExpTimeResponse.setExpirationTime(query.getDefaultGateway().getOAuthExpTime());
            return oAuthExpTimeResponse;
        } catch (final StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
    }

    public void setOAuthExpTime(final Integer expTime) {
        try {
            final List<GatewayBean> gateways = query.getAllGateways();
            for (final GatewayBean gw : gateways) {
                gw.setOAuthExpTime(expTime);
                storage.updateGateway(gw);
            }
        } catch (final StorageException e) {
            throw new ApplicationNotFoundException(e.getMessage());
        }
    }

    public JWTExpTimeResponse getJWTExpTime() {
        try {
            final Integer exptime = gatewayFacade.get(gatewayFacade.getDefaultGateway().getId()).getJWTExpTime();
            final JWTExpTimeResponse response = new JWTExpTimeResponse();
            response.setExpirationTime(exptime);
            return response;
        } catch (final StorageException e) {
            throw new JwtException("Could not return the JWT expiration time:" + e.getMessage());
        }
    }

    public void setJWTExpTime(final Integer expTime) {
        try {
            final UpdateGatewayBean updateGatewayBean = new UpdateGatewayBean();
            updateGatewayBean.setJwtExpTime(expTime);
            gatewayFacade.update(gatewayFacade.getDefaultGateway().getId(), updateGatewayBean);
        } catch (final StorageException e) {
            throw new JwtException("Could not update the JWT expiration time:" + e.getMessage());
        }
    }

    public Set<NewApiKeyBean> reissueAllApiKeys() {
        final Set<NewApiKeyBean> rval = new HashSet<>();
        for (final ApplicationVersionBean avb : getAllNonRetiredApplicationVersions()) {
            try {
                final NewApiKeyBean nakb = orgFacade.reissueApplicationVersionApiKey(avb);
                if (nakb != null) {
                    rval.add(nakb);
                }
            } catch (final Exception ex) {
                //Log the error, but continue the reissuance process
                _LOG.error("Key Auth Reissuance FAILED for {}, caused by:{}", ConsumerConventionUtil.createAppUniqueId(avb), ex);
            }
        }
        return rval;
    }

    public Set<NewOAuthCredentialsBean> reissueAllOAuthCredentials() {
        final Set<NewOAuthCredentialsBean> rval = new HashSet<>();
        for (final ApplicationVersionBean avb : getAllNonRetiredApplicationVersions()) {
            try {
                final NewOAuthCredentialsBean nocb = orgFacade.reissueApplicationVersionOAuthCredentials(avb);
                if (nocb != null) {
                    rval.add(nocb);
                }
            } catch (final Exception ex) {
                //Log the error, but continue the reissuance process
                _LOG.error("OAuth2 Credentials Reissuance FAILED for {}, caused by:{}", ConsumerConventionUtil.createAppUniqueId(avb), ex);
            }
        }
        return rval;
    }

    private List<ApplicationVersionBean> getAllNonRetiredApplicationVersions() {
        try {
            return query.getAllNonRetiredApplicationVersions();
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void revokeOAuthToken(final String accessToken) {
        try {
            query.getAllGateways().stream()
                    .map(GatewayBean::getId)
                    .map(gatewayFacade::createGatewayLink)
                    .forEach(gw -> gw.revokeGatewayOAuthToken(accessToken));
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public List<IdpIssuerBean> getIdpIssuers() {
        try {
            return query.getAllIdpIssuers();
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public IdpIssuerBean getIdpIssuer(final String issuerId) {
        try {
            final IdpIssuerBean iib = storage.getIdpIssuer(issuerId);
            if (iib == null) {
                throw ExceptionFactory.idpIssuerNotFoundException(issuerId);
            }
            return iib;
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public IdpIssuerBean createIdpIssuer(final IdpIssuerBean idpIssuer) {
        try {
            if (getIdpIssuer(idpIssuer.getIssuer()) != null) {
                throw ExceptionFactory.idpIssuerAlreadyExistsException(idpIssuer.getIssuer());
            }
            storage.createIdpIssuer(idpIssuer);
            return idpIssuer;
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public IdpIssuerBean updateIdpIssuer(final IdpIssuerBean idpIssuer) {
        try {
            final IdpIssuerBean existingIssuer = getIdpIssuer(idpIssuer.getIssuer());
            existingIssuer.setJwksUri(idpIssuer.getJwksUri());
            storage.updateIdpIssuer(existingIssuer);
            return existingIssuer;
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void deleteIdpIssuer(final String issuerId) {
        try {
            final IdpIssuerBean idpIssuer = getIdpIssuer(issuerId);
            storage.deleteIdpIssuer(idpIssuer);
        } catch (final StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public boolean isAdminApp() {
        try {
            return query.findManagedApplication(appCtx.getApplicationPrefix()).getType().equals(ManagedApplicationTypes.Admin);
        } catch (final StorageException e) {
            e.printStackTrace();
            return false;
        }
    }
}
