package com.t1t.apim.core.metrics;

import com.google.gson.Gson;
import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.metrics.AppUsageBean;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.metrics.ServiceUsageBean;
import com.t1t.apim.beans.policies.Policies;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.kong.model.DataDogMetricsQuery;
import com.t1t.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 07/09/16.
 */
public class DataDogMetricsSP implements MetricsSPI, Serializable {
    //private static Logger log = LoggerFactory.getLogger(DataDogMetricsSP.class.getName());

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
    @Inject private IStorageQuery query;
    private DataDogDBMetricsClient httpClient;

    public DataDogMetricsSP() {
        this.httpClient = new RestDataDogMetricsBuilder().getService(config.getDataDogMetricsURI(), config.getDataDogMetricsApiKey(), config.getDataDogMetricsApplicationKey(), DataDogDBMetricsClient.class);
    }

    @Override
    public ServiceUsageBean getServiceUsage(ServiceVersionBean service, DateTime from, DateTime to) {
        ServiceUsageBean rval = null;
        if (checkPolicy(service)) {
            String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(service);
            List<String> availableQueries = getAvailableMetrics(ids[0], ids[1], ids[2]);
            String finalQuery = consolidateQueries(availableQueries);
            rval.setData(new JSONObject(new Gson().toJson(query(from, to, finalQuery))));
        }
        return rval;
    }

    @Override
    public AppUsageBean getAppUsageForService(ServiceVersionBean service, String consumerId, DateTime from, DateTime to) {
        AppUsageBean rval = null;
        if (checkPolicy(service)) {
            //TODO - implementation
        }
        return rval;
    }

    @Override
    public ServiceMarketInfoBean getServiceMarketInfo(ServiceVersionBean service) {
        ServiceMarketInfoBean rval = null;
        if (checkPolicy(service)) {
            int distinctUsers = 0;
            int followers = 0;
            String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(service);
            try {
                distinctUsers = query.getServiceContracts(ids[0], ids[1], ids[2]).size();
                followers = service.getService().getFollowers().size();
            }
            catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            catch (StorageException ex) {
                throw ExceptionFactory.systemErrorException(ex);
            }
            rval.setDistinctUsers(distinctUsers);
            rval.setFollowers(followers);
            rval.setUptime(getServiceUptime(ids[0], ids[1], ids[2]).intValue());
        }
        return rval;
    }

    /*public static void main (String [] args){
        RestDataDogMetricsBuilder metricsBuilder = new RestDataDogMetricsBuilder();
        DataDogMetricsSP client = new DataDogMetricsSP();
        //System.out.println(client.getStatusCodeStats("trust1team","t1c-ds","v1",HistogramIntervalType.day,DateTime.now().minusDays(1), DateTime.now()).toString());
        //System.out.println(new Gson().toJson(query(DateTime.now().minusWeeks(2), DateTime.now(), "sum:kong.trust1team_t1c_ds_v1.request.count{env:prod}")));
        //System.out.println(getServiceUptime("trust1team", "t1c-ds", "v1"));

    }*/

    private Double getServiceUptime(String organizationId, String serviceId, String version) {
        Double uptimePercentage = null;
        List<String> errorQueries = new ArrayList<>();
        String totalQuery = null;
        List<String> availableQueries = getAvailableMetrics(organizationId, serviceId, version);

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
        DataDogMetricsQuery results = query(DateTime.now().minusWeeks(2), DateTime.now(), buildUptimeQuery(totalQuery, errorQueries));
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
                builder.append(QUERY_SEPARATOR).append(String.format(CUMSUM_QUERY, String.format(QUERY, AVG_PREFIX, error, config.getEnvironment())));
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

    private String convertDateTimeToSecondsString(DateTime time) {
        String rval = null;
        if (time != null) {
            rval = String.valueOf(time.getMillis() / 1000);
        }
        return rval;
    }

    private boolean checkPolicy(ServiceVersionBean svb) {
        try {
            String[] ids = ServiceConventionUtil.getOrgSvcVersionIds(svb);
            return !query.getEntityPoliciesByDefinitionId(ids[0], ids[1], ids[2], PolicyType.Service, Policies.DATADOG).isEmpty();
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }
}
