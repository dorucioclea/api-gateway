package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.services.ServiceVersionBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceMarketInfoFailSilent extends AbstractHystrixMetricsCommand<ServiceMarketInfoBean> {

    private final ServiceVersionBean service;


    public ServiceMarketInfoFailSilent(ServiceVersionBean service, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceMarketInfo"), timeout != null ? timeout : 200);

        this.service = service;
    }

    @Override
    protected ServiceMarketInfoBean run() {
        return this.getSpi().getServiceMarketInfo(service);
    }

    @Override
    protected ServiceMarketInfoBean getFallback() {
        return null;
    }
}