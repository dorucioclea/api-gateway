package com.t1t.apim.gateway;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.GatewayType;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.gateway.rest.GatewayValidation;
import com.t1t.apim.gateway.rest.RestGatewayLink;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Factory for creating gateway links.
 */
@ApplicationScoped
public class GatewayLinkFactory implements IGatewayLinkFactory {
    @Inject
    private IStorage storage;
    @Inject
    private AppConfig config;
    @Inject
    private GatewayValidation gatewayValidation;

    /**
     * @see IGatewayLinkFactory#create(GatewayBean)
     */
    @Override
    public IGatewayLink create(GatewayBean gateway) {
        if (gateway.getType() == GatewayType.REST) {
            return new RestGatewayLink(gateway, storage, config, gatewayValidation);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
