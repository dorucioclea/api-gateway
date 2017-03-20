package com.t1t.apim.scim;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.apim.beans.scim.SCIMConfigBean;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import java.io.UnsupportedEncodingException;

/**
 * Created by michallispashidis on 07/11/2015.
 * Application scoped bean, adding the header information to a VisiREG server instantce call.
 */
public class SCIMServiceBuilder {
    private static Logger _LOG = LoggerFactory.getLogger(SCIMServiceBuilder.class.getName());

    public SCIMServiceBuilder() {
    }

    /**
     * Provides the basic authentication header based on the username and password provided in the configuration.
     *
     * @param config
     * @return
     */
    private static synchronized String getBasicAuthValue(SCIMConfigBean config) {
        String authHeader = "";
        try {
            String username = config.getUsername();
            String password = config.getPassword();
            String up = username + ":" + password; //$NON-NLS-1$
            String base64 = null; //$NON-NLS-1$
            base64 = new String(Base64.encodeBase64(up.getBytes("UTF-8")));
            authHeader = "Basic " + base64; //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
    public <T> T getService(SCIMConfigBean config, Class<T> iFace) {
        //optional GSON converter
        //Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        StringBuilder scimURL = new StringBuilder(config.getEndpoint());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(scimURL.toString())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(msg -> _LOG.info("retrofit - SCIM:{}",msg))
                .setRequestInterceptor(requestFacade ->
                    requestFacade.addHeader("Authorization", getBasicAuthValue(config)))
                .build();
        _LOG.info("SCIM connection string:{}", scimURL.toString());
        return restAdapter.create(iFace);
    }
}
