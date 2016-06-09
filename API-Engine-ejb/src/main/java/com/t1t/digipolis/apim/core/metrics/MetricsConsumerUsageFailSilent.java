package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsConsumerUsageFailSilent extends HystrixCommand<MetricsConsumerUsageList> {
    private final MetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final String interval;
    private final String from;
    private final String to;
    private final String consumerId;


    public MetricsConsumerUsageFailSilent(MetricsClient client, String organizationId, String serviceId, String version, String interval, String from, String to, String consumerId) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsConsumerUsage"), 2000);
        this.client = client;

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
        return client.getServiceConsumerUsageFromToInterval(organizationId, serviceId, version, interval, from, to, consumerId);
    }

    @Override
    protected MetricsConsumerUsageList getFallback() {
        return null;
    }
}
