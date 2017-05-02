package com.t1t.apim.core.metrics;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 * An interface used to access metrics information.  Typically metrics are
 * recorded at runtime by the API Gateway into some sort of data store (e.g.
 * mongodb).  Implementations of this interface
 * must know how to extract the metrics information in useful and common ways
 * from that data store.
 */
public interface MetricsSPI {

    /**
     * Returns available common service metrics, including metrics for the specified list of applications
     * @param service
     * @param applications
     * @param from
     * @param to
     * @return
     */
    ServiceMetricsBean getServiceMetrics(ServiceVersionBean service, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to);

    /**
     * * Provides the service uptime information.
     * @param serviceVersion
     * @return
     */
    Integer getServiceUptime(ServiceVersionBean serviceVersion);

    /**
     * Set the config file to be used by the implementation
     * @param config
     */
    void setConfig(AppConfig config);
}
