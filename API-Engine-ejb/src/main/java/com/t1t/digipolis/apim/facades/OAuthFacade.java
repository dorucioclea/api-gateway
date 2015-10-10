package com.t1t.digipolis.apim.facades;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthApplicationResponse;
import com.t1t.digipolis.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthResponseType;
import com.t1t.digipolis.apim.beans.authorization.OAuthServiceScopeResponse;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicySummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.kong.KongConstants;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.util.GatewayPathUtilities;
import com.t1t.digipolis.util.ServiceConventionUtil;
import com.t1t.digipolis.util.URIUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by michallispashidis on 23/09/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OAuthFacade {
    private static Logger log = LoggerFactory.getLogger(OAuthFacade.class.getName());
    @Inject
    IStorageQuery query;
    @Inject
    private IStorage storage;
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    private AppConfig config;
    private static String consentURI;

    {
        consentURI = null;
        if (config != null) {
            consentURI = new StringBuffer("").append(config.getOAuthConsentURI()).toString();
        }
    }

    /**
     * This method should be called only for the consumer registring the OAuth service, and thus not for each consumer using the OAuth
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
        //retrieve applicatin name and redirect URI.
        try {
            ApplicationVersionBean avb = query.getApplicationForOAuth(request.getAppOAuthId(), request.getAppOAuthSecret());
            if (avb == null)
                throw new ApplicationNotFoundException("Application not found with given OAuth2 clientId and clientSecret.");
            oauthRequest.setName(avb.getApplication().getName());
            if (StringUtils.isEmpty(avb.getOauthClientRedirect()))
                throw new OAuthException("The application must provide an OAuth2 redirect URL");
            oauthRequest.setRedirectUri(avb.getOauthClientRedirect());
            String defaultGateway = query.listGateways().get(0).getId();
            if (!StringUtils.isEmpty(defaultGateway)) {
                try {
                    IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                    response = gatewayLink.enableConsumerForOAuth(request.getUniqueUserName(), oauthRequest);
                } catch (Exception e) {
                    ;//don't do anything
                }
                if (response == null) {
                    //try to recover existing user
                    try {
                        IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
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
            Preconditions.checkNotNull(query.listGateways().size()>0);
            String defaultGateway = query.listGateways().get(0).getId();
            response.setAuthorizationUrl(getOAuth2AuthorizeEndpoint(orgId, serviceId, version));
            response.setTokenUrl(getOAuth2TokenEndpoint(orgId, serviceId, version));
            if (!StringUtils.isEmpty(defaultGateway)) {
                try {
                    IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                    KongPluginOAuthConsumerResponseList appInfoList = gatewayLink.getApplicationOAuthInformation(clientId);
                    if (appInfoList != null && appInfoList.getData() != null && appInfoList.getData().size()>0) {
                        response.setConsumerResponse(appInfoList.getData().get(0));
                    }
                    ApplicationVersionBean applicationForOAuth = query.getApplicationForOAuth(response.getConsumerResponse().getClientId(), response.getConsumerResponse().getClientSecret());
                    response.setBase64AppLogo(applicationForOAuth.getApplication().getBase64logo());
                    response.setAppVersion(applicationForOAuth.getVersion());
                    //retrieve the Kong consumer
                    if (appInfoList.getData() != null && appInfoList.getData().size() > 0) {
                        String consumerId = appInfoList.getData().get(0).getConsumerId();
                        if (!StringUtils.isEmpty(consumerId)) {
                            response.setConsumer(gatewayLink.getConsumer(consumerId));
                        }
                    }
                } catch (Exception e) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e); //$NON-NLS-1$
                }
                //add scope information to the response
                if (response.getConsumer() != null && response.getConsumerResponse() != null) {
                    //retrieve scopes for targeted service
                    ServiceVersionBean serviceVersion = storage.getServiceVersion(orgId, serviceId, version);
                    //verify if it's an OAuth enabled service
                    response.setScopes(serviceVersion.getOauthScopes());
                    response.setServiceProvisionKey(serviceVersion.getProvisionKey());
                    /*serviceVersion.getService().getBasepath()*/
                    return response;
                }
                ;
            } else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAuthorizationRedirect(OAuthResponseType responseType, String userId, String clientId, String orgId, String serviceId, String version, List<String> scopes) {
        //The consent page is published through the gateway itself, in order to add consent page policies.
        //The consent URI is where the page has been published as part of the API Engine.
        StringBuffer redirectURI = new StringBuffer("");
        redirectURI.append(consentURI)
                .append("?response_type=")
                .append(responseType.toString().toLowerCase())
                .append("&")
                .append("client_id=").append(clientId)
                .append("&")
                .append("org_id=").append(orgId)
                .append("&")
                .append("service_id=").append(serviceId)
                .append("&")
                .append("version=").append(version)
                .append("&")
                .append("authenticatedUserId=").append(userId)
                .append("&")
                .append("scopes=").append(StringUtils.join(scopes, ','));
        return redirectURI.toString();
    }

    /**
     * Returns a list of scopes available for a service.
     * @param appId
     * @param orgId
     * @param serviceId
     * @param version
     * @return
     */
    public OAuthServiceScopeResponse getServiceVersionScopes(String appId, String orgId, String serviceId, String version){
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

    public String getOAuth2AuthorizeEndpoint(String orgId, String serviceId, String version){
        //there must be a gateway
        try{
            Preconditions.checkNotNull(query.listGateways().size()>0);
            String defaultGateway = query.listGateways().get(0).getId();
            GatewayBean gateway = storage.getGateway(defaultGateway);
            ServiceVersionBean svb = storage.getServiceVersion(orgId,serviceId,version);
            Preconditions.checkNotNull(svb);
            //construct the target url
            StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriBackslashRemover(gateway.getEndpoint()))
                    .append(URIUtils.uriBackslashAppender(GatewayPathUtilities.generateGatewayContextPath(orgId,svb.getService().getBasepath(),version)))
                    .append(KongConstants.KONG_OAUTH_ENDPOINT+"/");
            return targetURI.toString()+KongConstants.KONG_OAUTH2_ENDPOINT_AUTH;
            //response.setTokenUrl(targetURI.toString()+"token");
        } catch (StorageException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getOAuth2TokenEndpoint(String orgId, String serviceId, String version){
        //there must be a gateway
        try{
            Preconditions.checkNotNull(query.listGateways().size()>0);
            String defaultGateway = query.listGateways().get(0).getId();
            GatewayBean gateway = storage.getGateway(defaultGateway);
            ServiceVersionBean svb = storage.getServiceVersion(orgId,serviceId,version);
            Preconditions.checkNotNull(svb);
            //construct the target url
            StringBuilder targetURI = new StringBuilder("").append(URIUtils.uriBackslashRemover(gateway.getEndpoint()))
                    .append(URIUtils.uriBackslashAppender(GatewayPathUtilities.generateGatewayContextPath(orgId,svb.getService().getBasepath(),version)))
                    .append(KongConstants.KONG_OAUTH_ENDPOINT+"/");
            return targetURI.toString()+KongConstants.KONG_OAUTH2_ENDPOINT_TOKEN;
        } catch (StorageException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Creates a gateway link given a gateway id.
     *
     * @param gatewayId
     */
    private IGatewayLink createGatewayLink(String gatewayId) throws PublishingException {
        try {
            GatewayBean gateway = storage.getGateway(gatewayId);
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            return link;
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PublishingException(e.getMessage(), e);
        }
    }
}
