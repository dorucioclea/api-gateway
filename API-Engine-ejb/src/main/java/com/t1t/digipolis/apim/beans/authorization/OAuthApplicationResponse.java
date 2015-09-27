package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by michallispashidis on 26/09/15.
 */
public class OAuthApplicationResponse {
    KongPluginOAuthConsumerResponse consumerResponse;
    KongConsumer consumer;
    Map<String,String> scopes;

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

    @Override
    public String toString() {
        return "OAuthApplicationResponse{" +
                "consumerResponse=" + consumerResponse +
                ", consumer=" + consumer +
                ", scopes=" + scopes +
                '}';
    }
}
