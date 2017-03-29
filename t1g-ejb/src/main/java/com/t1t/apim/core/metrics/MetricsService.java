package com.t1t.apim.core.metrics;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.AppUsageBean;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.metrics.ServiceUsageBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
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

    public ServiceUsageBean getServiceUsage(ServiceVersionBean service, DateTime from, DateTime to) {
        return getReturnValue(new ServiceUsageFailSilent(service, from, to, appConfig.getHystrixMetricsTimeout()), service);
    }

    public AppUsageBean getAppUsage(ServiceVersionBean service, String consumerId, DateTime from, DateTime to) {
        return getReturnValue(new AppUsageBeanFailSilent(service, consumerId, from, to, appConfig.getHystrixMetricsTimeout()), service);
    }
    
    public ServiceMarketInfoBean getServiceMarketInfo(ServiceVersionBean service) {
        return getReturnValue(new ServiceMarketInfoFailSilent(service, appConfig.getHystrixMetricsTimeout()), service);
    }

    private <T> T getReturnValue(AbstractHystrixMetricsCommand<T> command, ServiceVersionBean service) {
        T rval = null;
        try {
            Iterator<MetricsSPI> metrics = loader.iterator();
            while (rval == null && metrics.hasNext()) {
                rval = command.withSpi(metrics.next()).execute();
            }
        }
        catch (ServiceConfigurationError serviceError) {
            rval = null;
            serviceError.printStackTrace();

        }
        return rval;
    }
}