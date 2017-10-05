package com.t1t.apim.kong;

import com.google.gson.GsonBuilder;
import com.t1t.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.apim.kong.adapters.KongSafeTypeAdapterFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by michallispashidis on 07/08/2015.
 * Application scoped bean, adding the header information to a VisiREG server instantce call.
 */
public class KongServiceBuilder {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String CREDENTIAL_SEPARATOR = ":";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static Logger _LOG = LoggerFactory.getLogger(KongServiceBuilder.class.getName());

    /**
     * Provides the basic authentication header based on the username and password provided in the configuration.
     *
     * @param config
     * @return
     */
    private static synchronized String getBasicAuthValue(RestGatewayConfigBean config) {
        String authHeader = null;
        String username;
        String password;
        if (StringUtils.isNoneBlank((username = config.getUsername()), (password = config.getPassword()))) {
            String up = username + CREDENTIAL_SEPARATOR + password;
            String base64 = null;
            base64 = new String(Base64.encodeBase64(up.getBytes(StandardCharsets.UTF_8)));
            authHeader = BASIC_PREFIX + base64;
        }
        return authHeader;
    }

    /**
     * Returns the service requestes throught the restAdapter.
     *
     * @param iFace
     * @param <T>
     * @return
     */
    public <T> T getService(RestGatewayConfigBean config, Class<T> iFace) {
        StringBuilder kongURL = new StringBuilder(config.getEndpoint());
        String authHeader;
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(kongURL.toString())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(msg -> _LOG.info("retrofit - KONG:{}", msg))
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapterFactory(new KongSafeTypeAdapterFactory()).create()));
        if ((authHeader = getBasicAuthValue(config)) != null) {
            builder.setRequestInterceptor(requestFacade ->
                    requestFacade.addHeader(AUTHORIZATION_HEADER_NAME, authHeader));
        }
        _LOG.info("Kong connection string:{}", kongURL.toString());
        return builder.build().create(iFace);
    }
}
