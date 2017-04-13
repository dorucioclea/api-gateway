package com.t1t.apim.core.metrics;

import com.google.gson.Gson;
import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.ServiceBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.kong.model.DataDogMetricsQuery;
import com.t1t.kong.model.DataDogMetricsSeries;
import com.t1t.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.t1t.util.TimeUtil.*;

/**
 * Created by michallispashidis on 07/09/16.
 */
public class DataDogMetricsSP implements Serializable, MetricsSPI {

    private static final String REQUEST_COUNT = "request.count";
    private static final String REQUEST_SIZE = "request.size";
    private static final String RESPONSE_SIZE = "response.size";
    private static final String REQUEST_STATUS = "request.status.";
    private static final String LATENCY = "latency";
    private static final String COUNT = "count";
    private static final String USER_UNIQUES = "user.uniques";
    private static final String AS_COUNT_FUNCTION = "as_count()";
    private static final String AVG_PREFIX = "avg";
    private static final String GATEWAY = "kong.";
    private static final String QUERY = "%s:%s{env:%s}";
    private static final String AVG_QUERY = "avg:kong.%s.%s{env:%s}";
    private static final String CUMSUM_QUERY = "cumsum(%s)";
    private static final String QUERY_SEPARATOR = ",";

    @Inject private AppConfig config;
    private static DataDogDBMetricsClient httpClient;

    public DataDogMetricsSP() {
        this.httpClient = new RestDataDogMetricsBuilder().getService(config.getDataDogMetricsURI(), config.getDataDogMetricsApiKey(), config.getDataDogMetricsApplicationKey(), DataDogDBMetricsClient.class);
    }

    @Override
    public ServiceMetricsBean getServiceMetrics(ServiceVersionBean service, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to) {
        ServiceMetricsBean rval = new ServiceMetricsBean();
        String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(service);
        List<String> availableQueries = getAvailableMetrics(ids[0], ids[1], ids[2]);
        if (availableQueries.isEmpty()) return null;
        String finalQuery = consolidateQueries(availableQueries);
        DataDogMetricsQuery results = query(from, to, finalQuery);
        List<JSONObject> general = new ArrayList<>();
        Map<ApplicationVersionSummaryBean, List<JSONObject>> sorted = new HashMap<>();

        for (DataDogMetricsSeries series : results.getSeries()) {
            JSONObject serializedSeries = new JSONObject(new Gson().toJson(series));
            Boolean added = false;
            for (ApplicationVersionSummaryBean sum : applications) {
                String id = sum.getId();
                if (!added) {
                    if (series.getMetric().contains(id)) {
                        if (sorted.containsKey(sum)) {
                            sorted.get(sum).add(serializedSeries);
                            added = true;
                        }
                        else {
                            List<JSONObject> consumerSeries = new ArrayList<>();
                            consumerSeries.add(serializedSeries);
                            sorted.put(sum, consumerSeries);
                            added = true;
                        }
                    }
                }
            }
            if (!added) general.add(serializedSeries);
        }
        rval.setServiceData(general);
        rval.setApplicationData(sorted);

        return rval;
    }

    @Override
    public Integer getServiceUptime(ServiceVersionBean serviceVersion) {
        String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(serviceVersion);
        return getServiceUptime(ids[0], ids[1], ids[2]).intValue();
    }

    private Double getServiceUptime(String organizationId, String serviceId, String version) {
        Double uptimePercentage = null;
        List<String> errorQueries = new ArrayList<>();
        String totalQuery = null;
        List<String> availableQueries = getAvailableMetrics(organizationId, serviceId, version);
        if (availableQueries.isEmpty()) return null;
        for (String query : availableQueries) {
            if (query.contains(REQUEST_STATUS)) {
                Long httpStatusCode = Long.valueOf(query.substring(query.lastIndexOf(".") + 1));
                if (httpStatusCode >= ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT && httpStatusCode <= ErrorCodes.HTTP_STATUS_CODE_NETWORK_CONNECT_TIMEOUT_ERROR) {
                    errorQueries.add(query);
                }
            }
            if (query.endsWith(REQUEST_COUNT)) {
                totalQuery = query;
            }
        }
        if (errorQueries.isEmpty()) {
            return 100D;
        }
        DataDogMetricsQuery results = query(DateTime.now().minusDays(2), DateTime.now(), buildUptimeQuery(totalQuery, errorQueries));
        if (results != null) {
            Double totalCount = 0D;
            Double totalErrors = 0D;
            for (int i = 0; i < results.getSeries().size(); i++) {
                List<List<Double>> pointlist = results.getSeries().get(i).getPointlist();
                Double seriesTotal = pointlist.stream().map(array -> array.get(1)).filter(Objects::nonNull).max(Double::compareTo).get();
                if (i == 0) {
                    totalCount += seriesTotal;
                }
                else {
                    totalErrors += seriesTotal;
                }
            }
            if (totalCount != null && totalErrors != null && totalCount >= totalErrors && totalCount != 0D) {
                uptimePercentage = (1 - (totalErrors / totalCount)) * 100;
            }
        }
        return uptimePercentage;
    }

    private String buildUptimeQuery(String total, List<String> errors) {
        if (StringUtils.isNotEmpty(total)) {
            StringBuilder builder = new StringBuilder(String.format(CUMSUM_QUERY, String.format(QUERY, AVG_PREFIX, total, config.getEnvironment())));
            for (String error : errors) {
                builder.append(QUERY_SEPARATOR).append(String.format(CUMSUM_QUERY, String.format(QUERY, AVG_PREFIX, error, "prod")));
            }
            return builder.toString();
        }
        else return null;
    }

    private DataDogMetricsQuery query(DateTime from, DateTime to, String query) {
        DataDogMetricsQuery rval = httpClient.queryMeterics(convertDateTimeToSecondsString(from), convertDateTimeToSecondsString(to), query);
        return rval;
    }

    private List<String> getAvailableMetrics(String organizationId, String serviceId, String version) {
        return httpClient.getAvailableQueries(convertDateTimeToSecondsString(DateTime.now().minusWeeks(2)))
                .getMetrics().stream()
                .filter(metric -> metric.contains(ServiceConventionUtil.getDatadogUniqueName(organizationId, serviceId, version)))
                .collect(Collectors.toList());
    }

    private String consolidateQueries(List<String> queries) {
        StringBuilder rval = new StringBuilder();
        for (String query : queries) {
            if (rval.length() != 0) rval.append(QUERY_SEPARATOR);
            rval.append(String.format(QUERY, AVG_PREFIX, query, config.getEnvironment()));
        }
        return rval.toString();
    }
}
