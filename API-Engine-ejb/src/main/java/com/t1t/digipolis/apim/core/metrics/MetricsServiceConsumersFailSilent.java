package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.kong.model.MetricsServiceConsumerList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsServiceConsumersFailSilent extends HystrixCommand<MetricsServiceConsumerList> {
    private final MongoDBMetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;


    public MetricsServiceConsumersFailSilent(MongoDBMetricsClient client, String organizationId, String serviceId, String version, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsMarketInfo"), timeout != null ? timeout : 200);
        this.client = client;

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
    }

    @Override
    protected MetricsServiceConsumerList run() {
        return client.getServiceMarketInfo(organizationId, serviceId, version);
    }

    @Override
    protected MetricsServiceConsumerList getFallback() {
        return null;
    }
}