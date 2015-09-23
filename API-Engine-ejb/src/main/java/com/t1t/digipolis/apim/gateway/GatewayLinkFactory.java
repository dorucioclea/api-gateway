package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.gateway.rest.RestGatewayLink;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Factory for creating gateway links.
 */
@ApplicationScoped
public class GatewayLinkFactory implements IGatewayLinkFactory {
    @Inject private IStorage storage;
    /**
     * Constructor.
     */
    public GatewayLinkFactory() {
    }
    
    /**
     * @see IGatewayLinkFactory#create(GatewayBean)
     */
    @Override
    public IGatewayLink create(GatewayBean gateway) {
        if (gateway.getType() == GatewayType.REST) {
            return new RestGatewayLink(gateway,storage);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
