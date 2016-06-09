package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsServiceConsumerList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsServiceConsumersFailSilent extends HystrixCommand<MetricsServiceConsumerList> {
    private final MetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;


    public MetricsServiceConsumersFailSilent(MetricsClient client, String organizationId, String serviceId, String version) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsMarketInfo"), 2000);
        this.client = client;

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
    }

    @Override
    protected MetricsServiceConsumerList run() {
        return client.getServiceConsumers(organizationId, serviceId, version);
    }

    @Override
    protected MetricsServiceConsumerList getFallback() {
        return null;
    }
}