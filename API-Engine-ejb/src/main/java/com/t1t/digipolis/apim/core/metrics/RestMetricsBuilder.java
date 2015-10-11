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
 */
public class RestMetricsBuilder {
    private static Logger _LOG = LoggerFactory.getLogger(RestMetricsBuilder.class.getName());

    public RestMetricsBuilder() {
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
        _LOG.info("Metrics connection string:{}", metricsURL.toString());
        return restAdapter.create(iFace);
    }

}
