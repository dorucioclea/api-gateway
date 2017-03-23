package com.t1t.apim.gateway;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.gateways.GatewayType;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.gateway.rest.GatewayValidation;
import com.t1t.apim.gateway.rest.RestGatewayLink;
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
    @Inject private GatewayValidation gatewayValidation;
    
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
            return new RestGatewayLink(gateway,storage,metricsURI,config,gatewayValidation);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
