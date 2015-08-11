package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;

/**
 * A factory for creating gateway links.
 */
public interface IGatewayLinkFactory {
    
    /**
     * Creates an appropriate gateway link for the given gateway bean.  Allows 
     * publishing of services to the given gateway.
     * @param gateway the gateway
     * @return the gateway link
     */
    public IGatewayLink create(GatewayBean gateway);

}
