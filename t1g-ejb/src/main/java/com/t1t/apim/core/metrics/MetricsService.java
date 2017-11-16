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

    private static ServiceLoader<MetricsSPI> loader = ServiceLoader.load(MetricsSPI.class);

    @Inject
    @T1G
    private AppConfigBean config;
    @Inject
    private IStorageQuery query;

    public ServiceMetricsBean getServiceMetrics(ServiceVersionBean serviceVersion, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to) {
        return getReturnValue(new ServiceMetricsFailSilent(serviceVersion, applications, from, to, config.getHystrixMetricsTimeout()), serviceVersion);
    }

    public Integer getServiceUptime(ServiceVersionBean serviceVersion) {
        return getReturnValue(new ServiceUptimeFailSilent(serviceVersion, config.getHystrixMetricsTimeout()), serviceVersion);
    }

    private <T> T getReturnValue(AbstractHystrixMetricsCommand<T> command, ServiceVersionBean serviceVersion) {
        T rval = null;
        try {
            Iterator<MetricsSPI> metrics = loader.iterator();
            while (rval == null && metrics.hasNext()) {
                MetricsSPI service = metrics.next();
                service.setConfig(config);
                if (service instanceof DataDogMetricsSP) {
                    if (!query.getEntityPoliciesByDefinitionId(serviceVersion.getService().getOrganization().getId(), serviceVersion.getService().getId(), serviceVersion.getVersion(), PolicyType.Service, Policies.DATADOG).isEmpty()) {
                        rval = command.withSpi(service).execute();
                    }
                } else {
                    rval = command.withSpi(service).execute();
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            rval = null;
            serviceError.printStackTrace();
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
        return rval;
    }
}