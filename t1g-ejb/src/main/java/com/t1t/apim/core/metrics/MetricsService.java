package com.t1t.apim.core.metrics;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
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

    public ServiceMetricsBean getServiceMetrics(ServiceVersionBean service, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to) {
        return getReturnValue(new ServiceMetricsFailSilent(service, applications, from, to, appConfig.getHystrixMetricsTimeout()));
    }

    public Integer getServiceUptime(ServiceVersionBean serviceVersion) {
        return getReturnValue(new ServiceUptimeFailSilent(serviceVersion, appConfig.getHystrixMetricsTimeout()));
    }

    private <T> T getReturnValue(AbstractHystrixMetricsCommand<T> command) {
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