package com.t1t.apim.core.metrics;

import com.google.gson.Gson;
import com.t1t.apim.IConfig;
import com.t1t.apim.beans.metrics.HistogramIntervalType;
import com.t1t.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.kong.model.*;
import com.t1t.util.ServiceConventionUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by michallispashidis on 07/09/16.
 */
public class DataDogMetricsSP implements MetricsSPI, Serializable {
    //private static Logger log = LoggerFactory.getLogger(DataDogMetricsSP.class.getName());

    private static final String REQUEST_COUNT = "request.count";
    private static final String REQUEST_SIZE = "request_size";
    private static final String RESPONSE_SIZE = "response_size";
    private static final String LATENCY = "latency";
    private static final String COUNT = "count";
    private static final String USER_UNIQUES = "user.uniques";
    private static final String AS_COUNT_FUNCTION = "as_count()";
    private static final String AVG_PREFIX = "avg:";
    private static final String GATEWAY = "kong.";
    private static final String QUERY = "avg:kong.%s.%s{env:%s}";


    private static DataDogDBMetricsClient httpClient;


    /*{
        //read properties file
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        if(is!=null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else throw new RuntimeException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        Config config = ConfigFactory.load(properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE));
        if(config ==null) throw new RuntimeException("API Engine config not found");
        String metricsURI = "";
        if (config != null && StringUtils.isEmpty(metricsURI)) {
            metricsURI = new StringBuffer("")
                    .append(config.getString(IConfig.METRICS_SCHEME))
                    .append("://")
                    .append(config.getString(IConfig.METRICS_DNS))
                    .append((!StringUtils.isEmpty(config.getString(IConfig.METRICS_PORT))) ? ":" + config.getString(IConfig.METRICS_PORT) : "")
                    .append("/").toString();
            log.info("DataDog Metrics processor instantiated for URI: {}", metricsURI);
            //create metrics client instance
            RestDataDogMetricsBuilder restMongoMetricsBuilder = new RestDataDogMetricsBuilder();
            httpClient = restMongoMetricsBuilder.getService(metricsURI, DataDogDBMetricsClient.class);
        }else throw new RuntimeException("DataDogMetricsAccessor - Metrics are not initialized");
    }*/

    public static void main (String [] args){
        RestDataDogMetricsBuilder restMongoMetricsBuilder = new RestDataDogMetricsBuilder();
        httpClient = restMongoMetricsBuilder.getService("https://app.datadoghq.com", DataDogDBMetricsClient.class);
        DataDogMetricsSP client = new DataDogMetricsSP();
        System.out.println(DateTime.now().getMillis());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(DateTime.now().getMillis());
        try {
            URL uri = new URI("https://app.datadoghq.com/api/v1/query?api_key=***REMOVED***&application_key=***REMOVED***&from=1490191983&to=1490278383&query=avg%3Akong.trust1team_t1c_ds_v1.latency%7Benv%3Aprod%7D").toURL();
            URLConnection yc = uri.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));String inputLine;
            while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
            in.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(new Gson().toJson(client.getUsage("trust1team","t1c-ds","v1",HistogramIntervalType.day,DateTime.now().minusDays(1), DateTime.now())));
        System.out.println(DateTime.now().getMillis());
    }

    @Override
    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        MetricsUsageList resultUsageList = null;
        String queryString = buildDatadogQuery(organizationId, serviceId, version, REQUEST_COUNT, AS_COUNT_FUNCTION);

        DataDogMetricsQuery query = query(from, to, queryString);

        //get value list
        if (!query.getSeries().isEmpty()) {
            final DataDogMetricsSeries dataDogMetricsSerie = query.getSeries().get(0);
            final List<List<Double>> pointlist = dataDogMetricsSerie.getPointlist();
            //get interval
            final Double seriesInterval = dataDogMetricsSerie.getInterval();
            //get length
            final Double seriesLength = dataDogMetricsSerie.getLength();

            //will contain the final results
            resultUsageList = new MetricsUsageList();
            //we create a epoch map in order to add specific values at random and sort the key values
            Map<Long, MetricsUsage> processingMap = new TreeMap<>();

            //fill the map, transpose, array of timestamp and value
            pointlist.stream().forEach(point -> {
                processingMap.put(point.get(0).longValue(), new MetricsUsage().withInterval(point.get(0)).withCount(point.get(1)));
            });

/*        for (MetricsUsage originUsage : originList.getData()) {
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsUsage().withCount(originUsage.getCount()).withInterval(originUsage.getInterval()));
        }*/
            //prep result
            resultUsageList.setData(new ArrayList<MetricsUsage>(processingMap.values()));
            return resultUsageList;
        }
        return resultUsageList;
    }

    @Override
    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        MetricsResponseStatsList rval = new MetricsResponseStatsList();

        return rval;
    }

    @Override
    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String applicationId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        return null;
    }

    @Override
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        return null;
    }

    private static String buildDatadogQuery(String organizationId, String serviceId, String version, String request, String function) {
        StringBuilder query = new StringBuilder(String.format(QUERY, ServiceConventionUtil.getDatadogUniqueName(organizationId, serviceId, version), request, "prod"));
        if (StringUtils.isNotEmpty(function)) query.append(".").append(function);
        return query.toString();
    }

    private static DataDogMetricsQuery query(DateTime from, DateTime to, String query) {
        //return httpClient.queryMeterics("***REMOVED***","***REMOVED***","1490277831","1490272160", "avg:kong.trust1team_t1c_ds_v2.request_count{env:prod}.as_count()");
        return httpClient.queryMeterics("***REMOVED***","***REMOVED***", String.valueOf(from.getMillis() / 1000), String.valueOf(to.getMillis() / 1000), query);
    }
}
