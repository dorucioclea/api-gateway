package com.t1t.apim.rest;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.t1t.apim.beans.services.RestServiceConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.nio.charset.StandardCharsets;

/**
 * Created by michallispashidis on 07/08/2015.
 * Application scoped bean, adding the header information to a VisiREG server instantce call.
 */
public class RestServiceBuilder {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String CREDENTIAL_SEPARATOR = ":";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static Logger _LOG = LoggerFactory.getLogger(RestServiceBuilder.class.getName());

    /**
     * Provides the basic authentication header based on the username and password provided in the configuration.
     *
     * @param config
     * @return
     */
    private static synchronized String getBasicAuthValue(RestServiceConfig config) {
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
    public static synchronized <T> T getService(Class<T> iFace, RestServiceConfig config, TypeAdapterFactory typeAdapterFactory) {
        StringBuilder kongURL = new StringBuilder(config.getEndpoint());
        String authHeader;
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(kongURL.toString())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(msg -> _LOG.info("retrofit - REST:{}", msg));
        if ((authHeader = getBasicAuthValue(config)) != null) {
            builder.setRequestInterceptor(requestFacade ->
                    requestFacade.addHeader(AUTHORIZATION_HEADER_NAME, authHeader));
        }
        if (typeAdapterFactory != null) {
            builder.setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create()));
        }
        _LOG.info("REST connection string:{}", kongURL.toString());
        return builder.build().create(iFace);
    }

    public static synchronized <T> T getService(Class<T> iFace, RestServiceConfig restServiceConfig) {
        return getService(iFace, restServiceConfig, null);
    }
}
