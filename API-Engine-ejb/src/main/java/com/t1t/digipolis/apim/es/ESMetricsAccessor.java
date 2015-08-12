package com.t1t.digipolis.apim.es;

import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import com.t1t.digipolis.apim.core.logging.ApimanLogger;
import com.t1t.digipolis.apim.core.logging.IApimanLogger;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.DateHistogramAggregation;
import io.searchbox.core.search.aggregation.DateHistogramAggregation.DateHistogram;
import io.searchbox.core.search.aggregation.FilterAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An elasticsearch implementation of the {@link IMetricsAccessor} interface.  This
 * implementation knows how to get metrics/analytics information out of an
 * elasticsearch store.
 */
@ApplicationScoped @Alternative
public class ESMetricsAccessor implements IMetricsAccessor {

    private static final String INDEX_NAME = "apiman_metrics"; //$NON-NLS-1$

    @Inject @ApimanLogger(ESMetricsAccessor.class)
    IApimanLogger log;

    @Inject @Named("metrics")
    private JestClient esClient;

    /**
     * Constructor.
     */
    public ESMetricsAccessor() {
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsage(String, String, String, com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType, DateTime, DateTime)
     */
    @SuppressWarnings("nls")
    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version,
            HistogramIntervalType interval, DateTime from, DateTime to) {
        UsageHistogramBean rval = new UsageHistogramBean();
        Map<String, UsageDataPoint> index = generateHistogramSkeleton(rval, from, to, interval, UsageDataPoint.class);

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"histogram\" : {" +
                    "          \"date_histogram\" : {" +
                    "              \"field\" : \"requestStart\"," +
                    "              \"interval\" : \"${interval}\"" +
                    "          }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            params.put("interval", interval.name());
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            DateHistogramAggregation aggregation = aggregations.getDateHistogramAggregation("histogram");
            if (aggregation != null) {
                List<DateHistogram> buckets = aggregation.getBuckets();
                for (DateHistogram entry : buckets) {
                    String keyAsString = entry.getTimeAsString();
                    if (index.containsKey(keyAsString)) {
                        index.get(keyAsString).setCount(entry.getCount());
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * Generate the histogram buckets based on the time frame requested and the interval.  This will
     * add an entry for each 'slot' or 'bucket' in the histogram, setting the count to 0.
     * @param rval
     * @param from
     * @param to
     * @param interval
     */
    public static <T extends HistogramDataPoint> Map<String, T> generateHistogramSkeleton(HistogramBean<T> rval, DateTime from, DateTime to,
            HistogramIntervalType interval, Class<T> dataType) {
        Map<String, T> index = new HashMap<>();

        Calendar fromCal = from.toGregorianCalendar();
        Calendar toCal = to.toGregorianCalendar();

        switch(interval) {
            case day:
                fromCal.set(Calendar.HOUR_OF_DAY, 0);
                fromCal.set(Calendar.MINUTE, 0);
                fromCal.set(Calendar.SECOND, 0);
                fromCal.set(Calendar.MILLISECOND, 0);
                break;
            case hour:
                fromCal.set(Calendar.MINUTE, 0);
                fromCal.set(Calendar.SECOND, 0);
                fromCal.set(Calendar.MILLISECOND, 0);
                break;
            case minute:
                fromCal.set(Calendar.SECOND, 0);
                fromCal.set(Calendar.MILLISECOND, 0);
                break;
            case month:
                fromCal.set(Calendar.DAY_OF_MONTH, 1);
                fromCal.set(Calendar.HOUR_OF_DAY, 0);
                fromCal.set(Calendar.MINUTE, 0);
                fromCal.set(Calendar.SECOND, 0);
                fromCal.set(Calendar.MILLISECOND, 0);
                break;
            case week:
                fromCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                fromCal.set(Calendar.HOUR_OF_DAY, 0);
                fromCal.set(Calendar.MINUTE, 0);
                fromCal.set(Calendar.SECOND, 0);
                fromCal.set(Calendar.MILLISECOND, 0);
                break;
            default:
                break;
        }

        while (fromCal.before(toCal)) {
            String label = formatDateWithMillis(fromCal);
            T point;
            try {
                point = dataType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            point.setLabel(label);
            rval.addDataPoint(point);
            index.put(label, point);
            switch (interval) {
                case day:
                    fromCal.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case hour:
                    fromCal.add(Calendar.HOUR_OF_DAY, 1);
                    break;
                case minute:
                    fromCal.add(Calendar.MINUTE, 1);
                    break;
                case month:
                    fromCal.add(Calendar.MONTH, 1);
                    break;
                case week:
                    fromCal.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                default:
                    break;
            }
        }

        return index;

    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsagePerApp(String, String, String, DateTime, DateTime)
     */
    @SuppressWarnings("nls")
    @Override
    public UsagePerAppBean getUsagePerApp(String organizationId, String serviceId, String version,
            DateTime from, DateTime to) {
        UsagePerAppBean rval = new UsagePerAppBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"usage_by_app\" : {" +
                    "        \"terms\" : {" +
                    "          \"field\" : \"applicationId\"" +
                    "        }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            ApimanTermsAggregation aggregation = aggregations.getAggregation("usage_by_app", ApimanTermsAggregation.class); //$NON-NLS-1$
            if (aggregation != null) {
                List<ApimanTermsAggregation.Entry> buckets = aggregation.getBuckets();
                int counter = 0;
                for (ApimanTermsAggregation.Entry entry : buckets) {
                    rval.getData().put(entry.getKey(), entry.getCount());
                    counter++;
                    if (counter > 5) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsagePerPlan(String, String, String, DateTime, DateTime)
     */
    @SuppressWarnings("nls")
    @Override
    public UsagePerPlanBean getUsagePerPlan(String organizationId, String serviceId, String version,
            DateTime from, DateTime to) {
        UsagePerPlanBean rval = new UsagePerPlanBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"usage_by_plan\" : {" +
                    "        \"terms\" : {" +
                    "          \"field\" : \"planId\"" +
                    "        }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            ApimanTermsAggregation aggregation = aggregations.getAggregation("usage_by_plan", ApimanTermsAggregation.class); //$NON-NLS-1$
            if (aggregation != null) {
                List<ApimanTermsAggregation.Entry> buckets = aggregation.getBuckets();
                for (ApimanTermsAggregation.Entry entry : buckets) {
                    rval.getData().put(entry.getKey(), entry.getCount());
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStats(String, String, String, com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType, DateTime, DateTime)
     */
    @SuppressWarnings("nls")
    @Override
    public ResponseStatsHistogramBean getResponseStats(String organizationId, String serviceId,
            String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        ResponseStatsHistogramBean rval = new ResponseStatsHistogramBean();
        Map<String, ResponseStatsDataPoint> index = generateHistogramSkeleton(rval, from, to, interval, ResponseStatsDataPoint.class);

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"histogram\" : {" +
                    "          \"date_histogram\" : {" +
                    "              \"field\" : \"requestStart\"," +
                    "              \"interval\" : \"${interval}\"" +
                    "          }," +
                    "          \"aggs\" : {" +
                    "              \"total_failures\" : {" +
                    "                  \"filter\" : { \"term\": { \"failure\": true } }" +
                    "              }," +
                    "              \"total_errors\" : {" +
                    "                  \"filter\" : { \"term\": { \"error\": true } }" +
                    "              }" +
                    "          }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            params.put("interval", interval.name());
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            DateHistogramAggregation aggregation = aggregations.getDateHistogramAggregation("histogram");
            if (aggregation != null) {
                List<DateHistogram> buckets = aggregation.getBuckets();
                for (DateHistogram entry : buckets) {
                    String keyAsString = entry.getTimeAsString();
                    if (index.containsKey(keyAsString)) {
                        FilterAggregation totalFailuresAgg = entry.getFilterAggregation("total_failures");
                        FilterAggregation totalErrorsAgg = entry.getFilterAggregation("total_errors");
                        long failures = totalFailuresAgg.getCount();
                        long errors = totalErrorsAgg.getCount();
                        ResponseStatsDataPoint point = index.get(keyAsString);
                        point.setTotal(entry.getCount());
                        point.setFailures(failures);
                        point.setErrors(errors);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsSummary(String, String, String, DateTime, DateTime)
     */
    @Override
    @SuppressWarnings("nls")
    public ResponseStatsSummaryBean getResponseStatsSummary(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsSummaryBean rval = new ResponseStatsSummaryBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "    \"total_failures\" : {" +
                    "      \"filter\" : { \"term\": { \"failure\": true } }" +
                    "    }," +
                    "    \"total_errors\" : {" +
                    "      \"filter\" : { \"term\": { \"error\": true } }" +
                    "    }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);

            rval.setTotal(response.getTotal());
            rval.setFailures(response.getAggregations().getFilterAggregation("total_failures").getCount());
            rval.setErrors(response.getAggregations().getFilterAggregation("total_errors").getCount());
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsPerApp(String, String, String, DateTime, DateTime)
     */
    @Override
    @SuppressWarnings("nls")
    public ResponseStatsPerAppBean getResponseStatsPerApp(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsPerAppBean rval = new ResponseStatsPerAppBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"by_app\" : {" +
                    "        \"terms\" : {" +
                    "          \"field\" : \"applicationId\"" +
                    "        }," +
                    "        \"aggs\" : {" +
                    "          \"total_failures\" : {" +
                    "            \"filter\" : { \"term\": { \"failure\": true } }" +
                    "          }," +
                    "          \"total_errors\" : {" +
                    "            \"filter\" : { \"term\": { \"error\": true } }" +
                    "          }" +
                    "        }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            ApimanTermsAggregation aggregation = aggregations.getAggregation("by_app", ApimanTermsAggregation.class); //$NON-NLS-1$
            if (aggregation != null) {
                List<ApimanTermsAggregation.Entry> buckets = aggregation.getBuckets();
                int counter = 0;
                for (ApimanTermsAggregation.Entry entry : buckets) {
                    ResponseStatsDataPoint point = new ResponseStatsDataPoint();
                    point.setTotal(entry.getCount());
                    rval.addDataPoint(entry.getKey(), entry.getCount(), entry.getFilterAggregation("total_failures").getCount(),
                            entry.getFilterAggregation("total_errors").getCount());
                    counter++;
                    if (counter > 10) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsPerPlan(String, String, String, DateTime, DateTime)
     */
    @Override
    @SuppressWarnings("nls")
    public ResponseStatsPerPlanBean getResponseStatsPerPlan(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsPerPlanBean rval = new ResponseStatsPerPlanBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {" +
                    "    \"filtered\" : {" +
                    "      \"query\" : {" +
                    "        \"range\" : {" +
                    "          \"requestStart\" : {" +
                    "            \"gte\": \"${from}\"," +
                    "            \"lte\": \"${to}\"" +
                    "          }" +
                    "        }" +
                    "      }," +
                    "      \"filter\": {" +
                    "        \"and\" : [" +
                    "          { \"term\" : { \"serviceOrgId\" : \"${serviceOrgId}\" } }," +
                    "          { \"term\" : { \"serviceId\" : \"${serviceId}\" } }," +
                    "          { \"term\" : { \"serviceVersion\" : \"${serviceVersion}\" } }" +
                    "        ]" +
                    "      }" +
                    "    }" +
                    "  }," +
                    "  \"size\": 0, " +
                    "  \"aggs\" : {" +
                    "      \"by_plan\" : {" +
                    "        \"terms\" : {" +
                    "          \"field\" : \"planId\"" +
                    "        }," +
                    "        \"aggs\" : {" +
                    "          \"total_failures\" : {" +
                    "            \"filter\" : { \"term\": { \"failure\": true } }" +
                    "          }," +
                    "          \"total_errors\" : {" +
                    "            \"filter\" : { \"term\": { \"error\": true } }" +
                    "          }" +
                    "        }" +
                    "      }" +
                    "  }" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("serviceOrgId", organizationId.replace('"', '_'));
            params.put("serviceId", serviceId.replace('"', '_'));
            params.put("serviceVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            ApimanTermsAggregation aggregation = aggregations.getAggregation("by_plan", ApimanTermsAggregation.class); //$NON-NLS-1$
            if (aggregation != null) {
                List<ApimanTermsAggregation.Entry> buckets = aggregation.getBuckets();
                int counter = 0;
                for (ApimanTermsAggregation.Entry entry : buckets) {
                    ResponseStatsDataPoint point = new ResponseStatsDataPoint();
                    point.setTotal(entry.getCount());
                    rval.addDataPoint(entry.getKey(), entry.getCount(), entry.getFilterAggregation("total_failures").getCount(),
                            entry.getFilterAggregation("total_errors").getCount());
                    counter++;
                    if (counter > 10) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getAppUsagePerService(String, String, String, DateTime, DateTime)
     */
    @Override
    @SuppressWarnings("nls")
    public AppUsagePerServiceBean getAppUsagePerService(String organizationId, String applicationId,
            String version, DateTime from, DateTime to) {
        AppUsagePerServiceBean rval = new AppUsagePerServiceBean();

        try {
            String query =
                    "{" +
                    "  \"query\": {\n" +
                    "    \"filtered\" : {\n" +
                    "      \"query\" : {\n" +
                    "        \"range\" : {\n" +
                    "          \"requestStart\" : {\n" +
                    "            \"gte\": \"${from}\",\n" +
                    "            \"lte\": \"${to}\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"filter\": {\n" +
                    "        \"and\" : [\n" +
                    "          { \"term\" : { \"applicationOrgId\" : \"${applicationOrgId}\" } },\n" +
                    "          { \"term\" : { \"applicationId\" : \"${applicationId}\" } },\n" +
                    "          { \"term\" : { \"applicationVersion\" : \"${applicationVersion}\" } }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"size\": 0, \n" +
                    "  \"aggs\" : {\n" +
                    "      \"usage_by_service\" : {\n" +
                    "        \"terms\" : {\n" +
                    "          \"field\" : \"serviceId\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "  }\n" +
                    "}";
            Map<String, String> params = new HashMap<>();
            params.put("from", formatDate(from));
            params.put("to", formatDate(to));
            params.put("applicationOrgId", organizationId.replace('"', '_'));
            params.put("applicationId", applicationId.replace('"', '_'));
            params.put("applicationVersion", version.replace('"', '_'));
            StrSubstitutor ss = new StrSubstitutor(params);
            query = ss.replace(query);

            Search search = new Search.Builder(query).addIndex(INDEX_NAME).addType("request").build();
            SearchResult response = getEsClient().execute(search);
            MetricAggregation aggregations = response.getAggregations();
            ApimanTermsAggregation aggregation = aggregations.getAggregation("usage_by_service", ApimanTermsAggregation.class); //$NON-NLS-1$
            if (aggregation != null) {
                List<ApimanTermsAggregation.Entry> buckets = aggregation.getBuckets();
                for (ApimanTermsAggregation.Entry entry : buckets) {
                    rval.getData().put(entry.getKey(), entry.getCount());
                }
            }
        } catch (IOException e) {
            log.error(e);
        }

        return rval;

    }

    /**
     * @param date
     */
    protected static String formatDate(DateTime date) {
        return ISODateTimeFormat.dateTimeNoMillis().print(date);
    }

    /**
     * @param date
     */
    protected static String formatDateWithMillis(DateTime date) {
        return ISODateTimeFormat.dateTime().print(date);
    }

    /**
     * @param date
     */
    protected static String formatDate(Calendar date) {
        return DateFormatUtils.formatUTC(date.getTimeInMillis(), "yyyy-MM-dd'T'HH:mm:ss'Z'"); //$NON-NLS-1$
    }

    /**
     * @param date
     */
    protected static String formatDateWithMillis(Calendar date) {
        return DateFormatUtils.formatUTC(date.getTimeInMillis(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
    }

    /**
     * @return the esClient
     */
    public JestClient getEsClient() {
        return esClient;
    }

    /**
     * @param esClient the esClient to set
     */
    public void setEsClient(JestClient esClient) {
        this.esClient = esClient;
    }

}
