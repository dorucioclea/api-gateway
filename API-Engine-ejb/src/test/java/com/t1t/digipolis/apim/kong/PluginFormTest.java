package com.t1t.digipolis.apim.kong;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.rest.GatewayValidation;
import com.t1t.digipolis.kong.model.KongPluginRequestTransformer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

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
        assertNotNull(kongPluginRequestTransformer);
        _LOG.info(kongPluginRequestTransformer.toString());
        assertNotNull(kongPluginRequestTransformer.getAdd());
        assertNotNull(kongPluginRequestTransformer.getRemove());
        _LOG.info(kongPluginRequestTransformer.getAdd().toString());
        _LOG.info("" + kongPluginRequestTransformer.getAdd().getHeaders().size());
        assertNotNull(kongPluginRequestTransformer.getAdd().getHeaders());
        assertTrue(kongPluginRequestTransformer.getAdd().getHeaders().size()>0);
        for(String header:kongPluginRequestTransformer.getAdd().getHeaders()){
            _LOG.info("Header:{}",header);
            if(header==null)_LOG.info("found null header");
        }
        _LOG.info(kongPluginRequestTransformer.getRemove().toString());
        //create initConfig policy
        Policy initPol = new Policy();
        initPol.setPolicyImpl(Policies.REQUESTTRANSFORMER.getKongIdentifier());
        initPol.setPolicyJsonConfig(json);
        Policy resultPol = GatewayValidation.validateRequestTransformer(initPol);
        _LOG.info(resultPol.toString());
        assertNotNull(resultPol);
    }
}
