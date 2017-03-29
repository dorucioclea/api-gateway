package com.t1t.apim.core.metrics;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;

/**
 * Created by michallispashidis on 07/08/2015.
 */
public class RestDataDogMetricsBuilder {
    //private static Logger _LOG = LoggerFactory.getLogger(RestDataDogMetricsBuilder.class.getName());
    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(RestDataDogMetricsBuilder.class.getName());
    private final static String API_KEY_PARAM = "api_key";
    private final static String APPLICATION_KEY_PARAM = "application_key";

    /**
     * Returns the service requestes throught the restAdapter.
     *
     * @param iFace
     * @param <T>
     * @return
     */
    public <T> T getService(String uri, String apiKey, String applicationKey, Class<T> iFace) {
        //optional GSON converter
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(uri)
                .setRequestInterceptor(request-> {
                    request.addQueryParam(API_KEY_PARAM, apiKey);
                    request.addQueryParam(APPLICATION_KEY_PARAM, applicationKey);
                })
                //.setLog(msg -> log.info("retrofit - DATADOG:" + msg))
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
       // _LOG.info("Datadog Metrics connection string:{}", dataDog.toString());
        return restAdapter.create(iFace);
    }

}
