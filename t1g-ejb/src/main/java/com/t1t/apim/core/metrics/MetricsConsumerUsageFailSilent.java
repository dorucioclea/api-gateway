package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.HistogramIntervalType;
import com.t1t.kong.model.MetricsConsumerUsageList;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsConsumerUsageFailSilent extends AbstractHystrixMetricsCommand<MetricsConsumerUsageList> {

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final HistogramIntervalType interval;
    private final DateTime from;
    private final DateTime to;
    private final String consumerId;


    public MetricsConsumerUsageFailSilent(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsConsumerUsage"), timeout != null ? timeout : 200);

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.interval = interval;
        this.from = from;
        this.to = to;
        this.consumerId = consumerId;
    }

    @Override
    protected MetricsConsumerUsageList run() {
        return this.getSpi().getAppUsageForService(organizationId, serviceId, version, interval, from, to, consumerId);
    }

    @Override
    protected MetricsConsumerUsageList getFallback() {
        return null;
    }
}
