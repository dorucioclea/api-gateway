package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import com.t1t.digipolis.apim.gateway.rest.RestGatewayLink;

import javax.enterprise.context.ApplicationScoped;

/**
 * Factory for creating gateway links.
 */
@ApplicationScoped
public class GatewayLinkFactory implements IGatewayLinkFactory {

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
            return new RestGatewayLink(gateway);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
