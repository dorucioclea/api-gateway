package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.digipolis.kong.model.MetricsServiceConsumerList;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceMarketInfoFailSilent extends AbstractHystrixMetricsCommand<ServiceMarketInfo> {

    private final String organizationId;
    private final String serviceId;
    private final String version;


    public ServiceMarketInfoFailSilent(String organizationId, String serviceId, String version, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceMarketInfo"), timeout != null ? timeout : 200);

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
    }

    @Override
    protected ServiceMarketInfo run() {
        return this.getSpi().getServiceMarketInfo(organizationId, serviceId, version);
    }

    @Override
    protected ServiceMarketInfo getFallback() {
        return null;
    }
}