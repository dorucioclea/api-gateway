package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by michallispashidis on 30/09/15.
 */
public class PluginFormTest {
    private static Logger _LOG = LoggerFactory.getLogger(PluginFormTest.class.getName());

    @Test
    public void testRequestTransformerFromJSON(){
        String json = "{\"remove\":{\"querystring\":[null],\"form\":[null],\"headers\":[\"someheader\"]},\"add\":{\"querystring\":[null],\"form\":[null],\"headers\":[\"another:test\",null]}}";
        Gson gson = new Gson();
        KongPluginRequestTransformer kongPluginRequestTransformer = gson.fromJson(json, KongPluginRequestTransformer.class);
        _LOG.info(kongPluginRequestTransformer.toString());
        _LOG.info(kongPluginRequestTransformer.getAdd().toString());
        _LOG.info(kongPluginRequestTransformer.getRemove().toString());
    }
}
