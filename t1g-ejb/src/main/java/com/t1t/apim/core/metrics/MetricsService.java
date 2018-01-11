package com.t1t.apim.core.metrics;

import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
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

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    private static ServiceLoader<MetricsSPI> loader = ServiceLoader.load(MetricsSPI.class);

    @Inject
    @T1G
    private AppConfigBean config;
    @Inject
    private IStorageQuery query;

    public ServiceMetricsBean getServiceMetrics(ServiceVersionBean serviceVersion, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to) {
        log.info("Retrieving metrics for: {}", serviceVersion.getService().getName());
        return getReturnValue(new ServiceMetricsFailSilent(serviceVersion, applications, from, to, config.getHystrixMetricsTimeout()), serviceVersion);
    }

    public Integer getServiceUptime(ServiceVersionBean serviceVersion) {
        return getReturnValue(new ServiceUptimeFailSilent(serviceVersion, config.getHystrixMetricsTimeout()), serviceVersion);
    }

    private <T> T getReturnValue(AbstractHystrixMetricsCommand<T> command, ServiceVersionBean serviceVersion) {
        T rval = null;
        try {
            log.info("loader: {}", loader);
            Iterator<MetricsSPI> metrics = loader.iterator();
            log.info("Metrics Providers has next: {}", metrics.hasNext());
            while (rval == null && metrics.hasNext()) {
                MetricsSPI service = metrics.next();
                service.setConfig(config);
                if (service instanceof DataDogMetricsSP) {
                    log.info("Metrics service is instance of DataDogMetricsSP");
                    if (!query.getEntityPoliciesByDefinitionId(serviceVersion.getService().getOrganization().getId(), serviceVersion.getService().getId(), serviceVersion.getVersion(), PolicyType.Service, Policies.DATADOG).isEmpty()) {
                        log.info("Service has DataDog policy");
                        rval = command.withSpi(service).execute();
                    }
                    else {
                        log.info("Service has no DataDog Policy");
                    }
                } else {
                    rval = command.withSpi(service).execute();
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            rval = null;
            log.error("Metrics error: ", serviceError);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        return rval;
    }
}