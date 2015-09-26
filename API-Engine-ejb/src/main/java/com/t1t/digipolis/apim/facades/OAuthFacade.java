package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthApplicationResponse;
import com.t1t.digipolis.apim.beans.authorization.OAuthConsumerRequestBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.services.ServiceGatewayBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
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
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerRequest;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.gateway.GatewayException;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by michallispashidis on 23/09/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OAuthFacade {
    @Inject @APIEngineContext private Logger log;
    @Inject IStorageQuery query;
    @Inject private IStorage storage;
    @Inject private IGatewayLinkFactory gatewayLinkFactory;

    public KongPluginOAuthConsumerResponse enableOAuthForConsumer(OAuthConsumerRequestBean request){
        //get the application version based on provided client_id and client_secret - we need the name and
        //TODO validate if non existing
        KongPluginOAuthConsumerRequest oauthRequest = new KongPluginOAuthConsumerRequest()
                .withClientId(request.getAppOAuthId())
                .withClientSecret(request.getAppOAuthSecret());
        KongPluginOAuthConsumerResponse response = null;
        //retrieve applicatin name and redirect URI.
        try {
            ApplicationVersionBean avb = query.getApplicationForOAuth(request.getAppOAuthId(),request.getAppOAuthSecret());
            if(avb==null)throw new ApplicationNotFoundException("Application not found with given OAuth2 clientId and clientSecret.");
            oauthRequest.setName(avb.getApplication().getName());
            if(StringUtils.isEmpty(avb.getOauthClientRedirect()))throw new OAuthException("The application must provide an OAuth2 redirect URL");
            oauthRequest.setRedirectUri(avb.getOauthClientRedirect());
            String defaultGateway = query.listGateways().get(0).getId();
            if(!StringUtils.isEmpty(defaultGateway)){
                try {
                    IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                    response = gatewayLink.enableConsumerForOAuth(request.getUniqueUserName(),oauthRequest);
                } catch (Exception e) {
                    ;//don't do anything
                }
                if (response==null){
                    //try to recover existing user
                    try {
                        IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                        KongPluginOAuthConsumerResponseList credentials = gatewayLink.getConsumerOAuthCredentials(request.getUniqueUserName());
                        for(KongPluginOAuthConsumerResponse cred:credentials.getData()){
                            if(cred.getClientId().equals(request.getAppOAuthId()))response = cred;
                        }
                    } catch (Exception e) {
                        //now throw an error if that's not working too.
                        throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e);
                    }
                }
            }else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return response;
    }

    public OAuthApplicationResponse getApplicationOAuthInformation(String clientId){
        OAuthApplicationResponse response = new OAuthApplicationResponse();
        try {
            String defaultGateway = query.listGateways().get(0).getId();
            if(!StringUtils.isEmpty(defaultGateway)){
                try {
                    IGatewayLink gatewayLink = createGatewayLink(defaultGateway);
                    KongPluginOAuthConsumerResponseList appInfoList = gatewayLink.getApplicationOAuthInformation(clientId);
                    if(appInfoList!=null && appInfoList.getData()!=null)response.setConsumerResponse(appInfoList.getData().get(0));
                    //retrieve the Kong consumer
                    if(appInfoList.getData()!=null && appInfoList.getData().size()>0){
                        String consumerId = appInfoList.getData().get(0).getConsumerId();
                        if(!StringUtils.isEmpty(consumerId)){
                            response.setConsumer(gatewayLink.getConsumer(consumerId));
                        }
                    }
                } catch (Exception e) {
                    throw ExceptionFactory.actionException(Messages.i18n.format("OAuth error"), e); //$NON-NLS-1$
                }
            }else throw new GatewayException("No default gateway found!");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        //add scope information to the response
        if(response.getConsumer()!=null && response.getConsumerResponse()!=null){
            //retrieve scopes
            return response;
        }else return null;
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
