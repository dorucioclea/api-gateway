package com.t1t.digipolis.apim.core.metrics;

import com.google.gson.JsonObject;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by michallispashidis on 07/09/16.
 */
public interface DataDogDBMetricsClient {
    /*********************
     * DATADOG METRICS ***
     *********************/

    static final String usage = "";
    static final String responseStats = "";
    static final String responseStatsSummary = "";
    static final String appUsageForService = "";
    static final String serviceMarketInfo = "";

    //https://app.datadoghq.com/api/v1/query?api_key=***REMOVED***&application_key=***REMOVED***&query=avg:kong.bza_citygis_v1.request.count{host:rasu094.rte.antwerpen.local}.as_count()&from=1473160678&to=1473247056

    @GET("/api/v1/query?api_key=***REMOVED***&application_key=***REMOVED***&query=avg:kong.bza_citygis_v1.request.count{host:rasu094.rte.antwerpen.local}.as_count()&from=1473160678&to=1473247056")
    JsonObject getUsage();

/*    @GET("/response-stats/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}")
    JsonObject getResponseStats(@Path("organizationId") String orgId,
                                              @Path("serviceId") String serviceId,
                                              @Path("version") String version,
                                              @Path("interval") String interval,
                                              @Path("from") String from,
                                              @Path("to") String to
    );

    @GET("/response-summary/{organizationId}/{serviceId}/{version}/{from}/{to}")
    JsonObject getResponseStatsSummary(@Path("organizationId") String orgId,
                                                       @Path("serviceId") String serviceId,
                                                       @Path("version") String version,
                                                       @Path("from") String from,
                                                       @Path("to") String to
    );

    @GET("/consumer-usage/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}/{consumer}")
    JsonObject getAppUsageForService(@Path("organizationId") String orgId,
                                                   @Path("serviceId") String serviceId,
                                                   @Path("version") String version,
                                                   @Path("interval") String interval,
                                                   @Path("from") String from,
                                                   @Path("to") String to,
                                                   @Path("consumer") String consumerId
    );

    @GET("/consumers/{organizationId}/{serviceId}/{version}")
    JsonObject getServiceMarketInfo(@Path("organizationId") String orgId,
                                                    @Path("serviceId") String serviceId,
                                                    @Path("version") String version
    );*/
}
