package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.HistogramIntervalType;
import com.t1t.kong.model.MetricsUsageList;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsUsageListFailSilent extends AbstractHystrixMetricsCommand<MetricsUsageList> {

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final HistogramIntervalType interval;
    private final DateTime from;
    private final DateTime to;



    public MetricsUsageListFailSilent(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsUsageList"), timeout != null ? timeout : 200);

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.interval = interval;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsUsageList run() {
        return this.getSpi().getUsage(organizationId, serviceId, version, interval, from, to);
    }

    @Override
    protected MetricsUsageList getFallback() {
        return null;
    }
}