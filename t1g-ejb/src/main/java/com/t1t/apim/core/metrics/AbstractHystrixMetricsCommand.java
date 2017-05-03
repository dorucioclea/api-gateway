package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public abstract class AbstractHystrixMetricsCommand<R> extends HystrixCommand<R> {
    private MetricsSPI spi;

    public AbstractHystrixMetricsCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, executionIsolationThreadTimeoutInMilliseconds);
    }

    public AbstractHystrixMetricsCommand<R> withSpi(MetricsSPI client) {
        this.spi = client;
        return this;
    }

    public MetricsSPI getSpi() {
        return spi;
    }

    public void setSpi(MetricsSPI spi) {
        this.spi = spi;
    }
}