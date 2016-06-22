package com.t1t.digipolis.apim.gateway;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.gateway.rest.RestGatewayLink;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Factory for creating gateway links.
 */
@ApplicationScoped
public class GatewayLinkFactory implements IGatewayLinkFactory {
    @Inject private IStorage storage;
    @Inject private AppConfig config;
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
            String metricsURI = new StringBuffer("")
                    .append(config.getMetricsScheme())
                    .append("://")
                    .append(config.getMetricsURI())
                    .append((!StringUtils.isEmpty(config.getMetricsPort()))?":"+config.getMetricsPort():"")
                    .append("/").toString();
            return new RestGatewayLink(gateway,storage,metricsURI,config);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
