package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.AppUsageBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class AppUsageBeanFailSilent extends AbstractHystrixMetricsCommand<AppUsageBean> {

    private final ServiceVersionBean service;
    private final String consumerId;
    private final DateTime from;
    private final DateTime to;


    public AppUsageBeanFailSilent(ServiceVersionBean service, String consumerId, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("AppUsage"), timeout != null ? timeout : 200);

        this.service = service;
        this.consumerId = consumerId;
        this.from = from;
        this.to = to;
    }

    @Override
    protected AppUsageBean run() {
        return this.getSpi().getAppUsageForService(service, consumerId, from, to);
    }

    @Override
    protected AppUsageBean getFallback() {
        return null;
    }
}
