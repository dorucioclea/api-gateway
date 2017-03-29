package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.ServiceUsageBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ServiceUsageFailSilent extends AbstractHystrixMetricsCommand<ServiceUsageBean> {

    private final ServiceVersionBean service;
    private final DateTime from;
    private final DateTime to;


    public ServiceUsageFailSilent(ServiceVersionBean service, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceUsage"), timeout != null ? timeout : 200);

        this.service = service;
        this.from = from;
        this.to = to;;
    }

    @Override
    protected ServiceUsageBean run() {
        return this.getSpi().getServiceUsage(service);
    }

    @Override
    protected ServiceUsageBean getFallback() {
        return null;
    }
}