package com.t1t.apim.core.metrics;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;

/**
 * Created by michallispashidis on 07/08/2015.
 */
public class RestDataDogMetricsBuilder {
    //private static Logger _LOG = LoggerFactory.getLogger(RestDataDogMetricsBuilder.class.getName());
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(RestDataDogMetricsBuilder.class.getName());

    public RestDataDogMetricsBuilder() {}

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
        StringBuilder dataDog = new StringBuilder(URI);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(dataDog.toString())
                //.setLog(msg -> log.info("retrofit - DATADOG:" + msg))
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
       // _LOG.info("Datadog Metrics connection string:{}", dataDog.toString());
        return restAdapter.create(iFace);
    }

}
