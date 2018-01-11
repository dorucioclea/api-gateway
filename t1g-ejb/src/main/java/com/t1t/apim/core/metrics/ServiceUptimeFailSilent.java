package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.services.ServiceVersionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceUptimeFailSilent extends AbstractHystrixMetricsCommand<Integer> {

    private static final Logger log = LoggerFactory.getLogger(ServiceUptimeFailSilent.class);

    private final ServiceVersionBean service;

    public ServiceUptimeFailSilent(ServiceVersionBean service, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceMarketInfo"), timeout != null ? timeout : 200);

        this.service = service;
    }

    @Override
    protected Integer run() {
        return this.getSpi().getServiceUptime(service);
    }

    @Override
    protected Integer getFallback() {
        log.info("Hystrix fallback method called for Service Uptime Metrics");
        return null;
    }
}