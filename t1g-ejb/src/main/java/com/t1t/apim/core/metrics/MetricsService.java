package com.t1t.apim.core.metrics;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.HistogramIntervalType;
import com.t1t.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.kong.model.MetricsConsumerUsageList;
import com.t1t.kong.model.MetricsResponseStatsList;
import com.t1t.kong.model.MetricsResponseSummaryList;
import com.t1t.kong.model.MetricsUsageList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@ApplicationScoped
@Default
public class MetricsService {

    private static final Logger _LOG = LoggerFactory.getLogger(MetricsService.class);
    private static ServiceLoader<MetricsSPI> loader = ServiceLoader.load(MetricsSPI.class);

    @Inject
    private AppConfig appConfig;

    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return getReturnValue(new MetricsUsageListFailSilent(organizationId, serviceId, version, interval, from, to, appConfig.getHystrixMetricsTimeout()));
    }

    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return getReturnValue(new MetricsResponseStatsListFailSilent(organizationId, serviceId, version, interval, from, to, appConfig.getHystrixMetricsTimeout()));
    }

    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return getReturnValue(new MetricsResponseSummaryFailSilent(organizationId, serviceId, version, from, to, appConfig.getHystrixMetricsTimeout()));
    }

    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String applicationId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        return getReturnValue(new MetricsConsumerUsageFailSilent(organizationId, applicationId, version, interval, from, to, consumerId, appConfig.getHystrixMetricsTimeout()));
    }
    
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        return getReturnValue(new ServiceMarketInfoFailSilent(organizationId, serviceId, version, appConfig.getHystrixMetricsTimeout()));
    }

    private <T> T getReturnValue(AbstractHystrixMetricsCommand<T> command) {
        T rval = null;
        try {
            Iterator<MetricsSPI> metrics = loader.iterator();
            while (rval == null && metrics.hasNext()) {
                rval = command.setSpi(metrics.next()).execute();
            }
        } catch (ServiceConfigurationError serviceError) {
            rval = null;
            serviceError.printStackTrace();

        }
        return rval;
    }
}