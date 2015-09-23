package com.t1t.digipolis.apim.core.metrics;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import java.io.UnsupportedEncodingException;

/**
 * Created by michallispashidis on 07/08/2015.
 * Application scoped bean, adding the header information to a VisiREG server instantce call.
 */
public class RestMetricsBuilder {
    private static Logger _LOG = LoggerFactory.getLogger(RestMetricsBuilder.class.getName());

    public RestMetricsBuilder() {
    }

    /**
     * Provides the basic authentication header based on the username and password provided in the configuration.
     *
     * @param config
     * @return
     */
    private static synchronized String getBasicAuthValue(RestGatewayConfigBean config) {
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
    public <T> T getService(String URI, Class<T> iFace) {
        //optional GSON converter
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        StringBuilder metricsURL = new StringBuilder(URI);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(metricsURL.toString()).build();
/*                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        String authHeader = getBasicAuthValue(config);
                        requestFacade.addHeader("Authorization", getBasicAuthValue(config));
                    }
                })*/
/*                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        switch (retrofitError.getResponse().getStatus()) {
                            case ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT:
                                throw new GatewayNotFoundException(retrofitError.getMessage());
                            case ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE:
                                throw new AlreadyExistsException(retrofitError.getResponse().getReason());
                            default:
                                throw new RuntimeException("");
                        }
                    }
                })*/
        _LOG.info("Metrics connection string:{}", metricsURL.toString());
        return restAdapter.create(iFace);
    }

}
