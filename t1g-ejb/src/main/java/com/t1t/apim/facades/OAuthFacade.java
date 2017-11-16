package com.t1t.apim.facades;

import com.google.common.base.Preconditions;
import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.authorization.OAuthApplicationResponse;
import com.t1t.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.apim.beans.authorization.OAuthServiceScopeResponse;
import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ApplicationNotFoundException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.OAuthException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.gateway.IGatewayLink;
import com.t1t.apim.rest.KongConstants;
import com.t1t.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.util.GatewayPathUtilities;
import com.t1t.util.URIUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 23/09/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OAuthFacade {
    @Inject
    private IStorageQuery query;
    @Inject
    private IStorage storage;
    @Inject
    private IIdmStorage idmStorage;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private AppConfig config;

    /**
     * This method should be called only for the consumer registering the OAuth service, and thus not for each consumer using the OAuth
     *
     * @param request
     * @return
     */
    public KongPluginOAuthConsumerResponse enableOAuthForConsumer(OAuthConsumerRequestBean request) {
        //get the application version based on provided client_id and client_secret - we need the name and
        //TODO validate if non existing
        KongPluginOAuthConsumerRequest oauthRequest = new KongPluginOAuthConsumerRequest()
                .withClientId(request.getAppOAuthId())
                .withClientSecret(request.getAppOAuthSecret());
        KongPluginOAuthConsumerResponse response = null;
        //retrieve application name and redirect URI.
        try {
            ApplicationVersionBean avb = query.getApplicationForOAuth(request.getAppOAuthId(), request.getAppOAuthSecret());
            if (avb == null)
                throw new ApplicationNotFoundException("Application not found with given OAuth2 clientId and clientSecret.");
            oauthRequest.setName(avb.getApplication().getName());
            if (avb.getOauthClientRedirects() == null || avb.getOauthClientRedirects().isEmpty() || avb.getOauthClientRedirects().stream().filter(redirect -> !StringUtils.isEmpty(redirect)).collect(Collectors.toSet()).isEmpty())
                throw new OAuthException("The application must provide an OAuth2 redirect URL");
            oauthRequest.setRedirectUri(avb.getOauthClientRedirects());
            String defaultGateway = query.listGateways().get(0).getId();
            if (!StringUtils.isEmpty(defaultGateway)) {
                try {
                    IGatewayLink gatewayLink = gatewayFacade.getDefaultGatewayLink();
                    response = gatewayLink.enableConsumerForOAuth(request.getUniqueUserName(), oauthRequest);
                    avb.setOauthCredentialId(response.getId());
                    storage.updateApplicationVersion(avb);
                } catch (Exception e) {
                    //don't do anything
                }
                if (response == null) {
                    //try to recover existing user
                    try {
                        IGatewayLink gatewayLink = gatewayFacade.getDefaultGatewayLink();
                        KongPluginOAuthConsumerResponseList credentials = gatewayLink.getConsumerOAuthCredentials(request.getUniqueUserName());
                        for (KongPluginOAuthConsumerResponse cred : credentials.getData()) {
                            if (cred.getClientId().equals(request.getAppOAuthId())) response = cred;
                        }
                    } catch (Exception e) {
                        //now throw an error if that's not working too.
                        throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                    }
                }
            } else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return response;
    }

    public OAuthApplicationResponse getApplicationOAuthInformation(String clientId, String orgId, String serviceId, String version) {
        OAuthApplicationResponse response = new OAuthApplicationResponse();
        try {
            //there must be a gateway
            ContractBean contract = query.getContractByServiceVersionAndOAuthClientId(orgId, serviceId, version, clientId);
            if (contract == null) {
                throw ExceptionFactory.applicationOAuthInformationNotFoundException(clientId, serviceId + " " + version);
            }
            Preconditions.checkArgument(query.listGateways().size() > 0);
            String defaultGateway = query.listGateways().get(0).getId();
            response.setAuthorizationUrl(getOAuth2AuthorizeEndpoint(orgId, serviceId, version));
            response.setTokenUrl(getOAuth2TokenEndpoint(orgId, serviceId, version));
            if (!StringUtils.isEmpty(defaultGateway)) {
                try {
                    IGatewayLink gatewayLink = gatewayFacade.getDefaultGatewayLink();
                    KongPluginOAuthConsumerResponseList appInfoList = gatewayLink.getApplicationOAuthInformation(clientId);
                    if (appInfoList != null && appInfoList.getData() != null && appInfoList.getData().size() > 0) {
                        response.setConsumerResponse(appInfoList.getData().get(0));
                    }
                    ApplicationVersionBean applicationForOAuth = query.getApplicationForOAuth(response.getConsumerResponse().getClientId(), response.getConsumerResponse().getClientSecret());
                    response.setBase64AppLogo(applicationForOAuth.getApplication().getBase64logo());
                    response.setAppVersion(applicationForOAuth.getVersion());
                } catch (StorageException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e); //$NON-NLS-1$
                }
                //add scope information to the response
                if (response.getConsumerResponse() != null) {
                    //retrieve scopes for targeted service
                    ServiceVersionBean serviceVersion = contract.getService();
                    //verify if it's an OAuth enabled service
                    response.setScopes(serviceVersion.getOauthScopes());
                    response.setServiceProvisionKey(serviceVersion.getProvisionKey());
                    /*serviceVersion.getService().getBasepath()*/
                    return response;
                }
            } else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of scopes available for a service.
     *
     * @param appId
     * @param orgId
     * @param serviceId
     * @param version
     * @return
     */
    public OAuthServiceScopeResponse getServiceVersionScopes(String appId, String orgId, String serviceId, String version) {
        //retrieve scopes for targeted service
        ServiceVersionBean serviceVersion = null;
        OAuthServiceScopeResponse serviceScopes = new OAuthServiceScopeResponse();
        try {
            //verify if it's an OAuth enabled service
            serviceVersion = storage.getServiceVersion(orgId, serviceId, version);//throw an error if non existant
            serviceScopes.setScopes(serviceVersion.getOauthScopes());
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return serviceScopes;
    }

    public String getOAuth2AuthorizeEndpoint(String orgId, String serviceId, String version) {
        //there must be a gateway
        try {
            String defaultGateway = query.listGateways().get(0).getId();
            GatewayBean gateway = storage.getGateway(defaultGateway);
            ServiceVersionBean svb = storage.getServiceVersion(orgId, serviceId, version);
            if (svb == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            //construct the target url
            //TODO - return a list of strings for all uris
            StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriBackslashRemover(gateway.getEndpoint()))
                    .append(URIUtils.uriBackslashAppender(GatewayPathUtilities.generateGatewayContextPath(orgId, svb.getService().getBasepaths(), version).get(0)))
                    .append(KongConstants.KONG_OAUTH_ENDPOINT + "/");
            return targetURI.toString() + KongConstants.KONG_OAUTH2_ENDPOINT_AUTH;
            //response.setTokenUrl(targetURI.toString()+"token");
        } catch (StorageException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getOAuth2TokenEndpoint(String orgId, String serviceId, String version) {
        //there must be a gateway
        try {
            String defaultGateway = query.listGateways().get(0).getId();
            GatewayBean gateway = storage.getGateway(defaultGateway);
            ServiceVersionBean svb = storage.getServiceVersion(orgId, serviceId, version);
            if (svb == null) {
                throw ExceptionFactory.serviceVersionNotFoundException(serviceId, version);
            }
            //construct the target url
            //TODO - return a list of strings for all uris
            StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriBackslashRemover(gateway.getEndpoint()))
                    .append(URIUtils.uriBackslashAppender(GatewayPathUtilities.generateGatewayContextPath(orgId, svb.getService().getBasepaths(), version).get(0)))
                    .append(KongConstants.KONG_OAUTH_ENDPOINT + "/");
            return targetURI.toString() + KongConstants.KONG_OAUTH2_ENDPOINT_TOKEN;
        } catch (StorageException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
