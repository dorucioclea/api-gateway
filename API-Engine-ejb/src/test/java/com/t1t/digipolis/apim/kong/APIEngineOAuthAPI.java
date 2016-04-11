package com.t1t.digipolis.apim.kong;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Created by michallispashidis on 05/04/16.
 */
public class APIEngineOAuthAPI extends DefaultApi20 {
    private static String serviceURL;
    public APIEngineOAuthAPI() {
    }
    public static APIEngineOAuthAPI instance(String url) {
        serviceURL = url;
        return APIEngineOAuthAPI.InstanceHolder.INSTANCE;
    }

    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    public String getAccessTokenEndpoint() {
        return serviceURL+"/oauth2/authorize";
    } //only implicit grant used for testing

    public String getRefreshTokenEndpoint() {
        throw new UnsupportedOperationException("APIEngine supports refresh tokens, but irrelevant for testing purpose");
    }

    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");
        StringBuilder sb = new StringBuilder(String.format("https://www.facebook.com/v2.5/dialog/oauth?client_id=%s&redirect_uri=%s", new Object[]{config.getApiKey(), OAuthEncoder.encode(config.getCallback())}));
        if(config.hasScope()) {
            sb.append('&').append("scope").append('=').append(OAuthEncoder.encode(config.getScope()));
        }
        String state = config.getState();
        if(state != null) {
            sb.append('&').append("state").append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    private static class InstanceHolder {
        private static final APIEngineOAuthAPI INSTANCE = new APIEngineOAuthAPI();
        private InstanceHolder() {
        }
    }
}
