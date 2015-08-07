package com.t1t.digipolis.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import javax.annotation.PostConstruct;
import javax.ejb.Init;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by michallispashidis on 07/08/2015.
 * Application scoped bean, adding the header information to a VisiREG server instantce call.
 */
@ApplicationScoped
public class RestServiceBuilder {
    @Inject
    @APIEngineContext
    private Logger _LOG;

    private static RestAdapter restAdapter;
    private static RequestInterceptor requestInterceptor;

    /**
     * Reset config method, also used for initialization of the REST servicebuilder.
     * The reset can be called when updating the external ID in headers.
     */
    @PostConstruct
    public void resetConfig(){
        Config config = ConfigFactory.load();

        //optional converter
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        //Default converter = Gson convertor
        StringBuilder kongURL = new StringBuilder(config.getString(KongConstants.KONG_SCHEME)).append("://")
                .append(config.getString(KongConstants.KONG_URI)).append(":")
                .append(config.getString(KongConstants.KONG_PORT_MGT)).append("/");

        restAdapter = new RestAdapter.Builder().setEndpoint(kongURL.toString())
                .build();//.setRequestInterceptor(requestInterceptor)
        _LOG.info("Kong connection string:{}",kongURL.toString());
    }

    /**
     * Returns the service requestes throught the restAdapter.
     *
     * @param iFace
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> iFace) {
        return restAdapter.create(iFace);
    }

}
