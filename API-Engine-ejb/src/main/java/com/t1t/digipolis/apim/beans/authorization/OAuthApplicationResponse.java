package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;

import java.util.Map;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class OAuthApplicationResponse {
    KongPluginOAuthConsumerResponse consumerResponse;
    KongConsumer consumer;
    Map<String,String> scopes;
    String base64AppLogo;
    String appVersion;
    String serviceProvisionKey;
    String targetUrl;

    public OAuthApplicationResponse() {
    }

    public KongPluginOAuthConsumerResponse getConsumerResponse() {
        return consumerResponse;
    }

    public void setConsumerResponse(KongPluginOAuthConsumerResponse consumerResponse) {
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

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
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
                ", targetUrl='" + targetUrl + '\'' +
                '}';
    }
}
