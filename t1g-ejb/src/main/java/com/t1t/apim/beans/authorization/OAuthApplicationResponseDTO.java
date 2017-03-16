package com.t1t.apim.beans.authorization;

import com.t1t.kong.model.KongConsumer;
import com.t1t.kong.model.KongPluginOAuthConsumerResponseDTO;

import java.util.Map;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class OAuthApplicationResponseDTO {
    KongPluginOAuthConsumerResponseDTO consumerResponse;
    KongConsumer consumer;
    Map<String,String> scopes;
    String base64AppLogo;
    String appVersion;
    String serviceProvisionKey;
    String authorizationUrl;
    String tokenUrl;

    public OAuthApplicationResponseDTO() {
    }

    public OAuthApplicationResponseDTO(OAuthApplicationResponse resp) {
        this.consumerResponse = new KongPluginOAuthConsumerResponseDTO().withId(resp.getConsumerResponse().getId())
                .withName(resp.getConsumerResponse().getName())
                .withConsumerId(resp.getConsumerResponse().getConsumerId())
                .withClientId(resp.getConsumerResponse().getClientId())
                .withClientSecret(resp.getConsumerResponse().getClientSecret())
                .withCreatedAt(resp.getConsumerResponse().getCreatedAt())
                //we only return one redirect uri for compatibility issues with consent app
                .withRedirectUri(resp.getConsumerResponse().getRedirectUri() != null
                        && !resp.getConsumerResponse().getRedirectUri().isEmpty() ?
                        resp.getConsumerResponse().getRedirectUri().iterator().next() : null);
        this.consumer = resp.getConsumer();
        this.scopes = resp.getScopes();
        this.base64AppLogo = resp.getBase64AppLogo();
        this.appVersion = resp.getAppVersion();
        this.serviceProvisionKey = resp.getServiceProvisionKey();
        this.authorizationUrl = resp.authorizationUrl;
        this.tokenUrl = resp.getTokenUrl();
    }

    public KongPluginOAuthConsumerResponseDTO getConsumerResponse() {
        return consumerResponse;
    }

    public void setConsumerResponse(KongPluginOAuthConsumerResponseDTO consumerResponse) {
        this.consumerResponse = consumerResponse;
    }

    public KongConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(KongConsumer consumer) {
        this.consumer = consumer;
    }

    public Map<String, String> getScopes() {
        return scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    public String getBase64AppLogo() {
        return base64AppLogo;
    }

    public void setBase64AppLogo(String base64AppLogo) {
        this.base64AppLogo = base64AppLogo;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getServiceProvisionKey() {
        return serviceProvisionKey;
    }

    public void setServiceProvisionKey(String serviceProvisionKey) {
        this.serviceProvisionKey = serviceProvisionKey;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    @Override
    public String toString() {
        return "OAuthApplicationResponse{" +
                "consumerResponse=" + consumerResponse +
                ", consumer=" + consumer +
                ", scopes=" + scopes +
                ", base64AppLogo='" + base64AppLogo + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", serviceProvisionKey='" + serviceProvisionKey + '\'' +
                ", authorizationUrl='" + authorizationUrl + '\'' +
                ", tokenUrl='" + tokenUrl + '\'' +
                '}';
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

}
