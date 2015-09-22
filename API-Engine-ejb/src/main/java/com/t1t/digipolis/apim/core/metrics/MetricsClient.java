package com.t1t.digipolis.apim.core.metrics;

import com.google.gson.JsonObject;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by michallispashidis on 7/08/15.
 */
public interface MetricsClient {
    /*********************
     * METRICS ***********
     *********************/
    @GET("/usage/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}")
    JsonObject getServiceUsageFromToInterval(@Path("organizationId") String orgId,
                                             @Path("serviceId") String serviceId,
                                             @Path("version") String version,
                                             @Path("interval") String interval,
                                             @Path("from") String from,
                                             @Path("to") String to
    );

    @GET("/response-stats/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}")
    JsonObject getServiceResponseStatisticsFromToInterval(@Path("organizationId") String orgId,
                                                          @Path("serviceId") String serviceId,
                                                          @Path("version") String version,
                                                          @Path("interval") String interval,
                                                          @Path("from") String from,
                                                          @Path("to") String to
    );

    @GET("/response-summary/{organizationId}/{serviceId}/{version}/{from}/{to}")
    JsonObject getServiceResponseSummaryFromTo(@Path("organizationId") String orgId,
                                               @Path("serviceId") String serviceId,
                                               @Path("version") String version,
                                               @Path("from") String from,
                                               @Path("to") String to
    );

    @GET("/consumer-usage/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}/{consumer}")
    JsonObject getServiceConsumerUsageFromToInterval(@Path("organizationId") String orgId,
                                                          @Path("serviceId") String serviceId,
                                                          @Path("version") String version,
                                                          @Path("interval") String interval,
                                                          @Path("from") String from,
                                                          @Path("to") String to,
                                                          @Path("consumer") String consumerId
    );

}
