package com.t1t.digipolis.apim.core.metrics;

import com.google.gson.JsonObject;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsServiceConsumerList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
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
    MetricsUsageList getServiceUsageFromToInterval(@Path("organizationId") String orgId,
                                                   @Path("serviceId") String serviceId,
                                                   @Path("version") String version,
                                                   @Path("interval") String interval,
                                                   @Path("from") String from,
                                                   @Path("to") String to
    );

    @GET("/response-stats/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}")
    MetricsResponseStatsList getServiceResponseStatisticsFromToInterval(@Path("organizationId") String orgId,
                                                                        @Path("serviceId") String serviceId,
                                                                        @Path("version") String version,
                                                                        @Path("interval") String interval,
                                                                        @Path("from") String from,
                                                                        @Path("to") String to
    );

    @GET("/response-summary/{organizationId}/{serviceId}/{version}/{from}/{to}")
    MetricsResponseSummaryList getServiceResponseSummaryFromTo(@Path("organizationId") String orgId,
                                                               @Path("serviceId") String serviceId,
                                                               @Path("version") String version,
                                                               @Path("from") String from,
                                                               @Path("to") String to
    );

    @GET("/consumer-usage/{organizationId}/{serviceId}/{version}/{interval}/{from}/{to}/{consumer}")
    MetricsConsumerUsageList getServiceConsumerUsageFromToInterval(@Path("organizationId") String orgId,
                                                                   @Path("serviceId") String serviceId,
                                                                   @Path("version") String version,
                                                                   @Path("interval") String interval,
                                                                   @Path("from") String from,
                                                                   @Path("to") String to,
                                                                   @Path("consumer") String consumerId
    );

    @GET("/consumers/{organizationId}/{serviceId}/{version}")
    MetricsServiceConsumerList getServiceConsumers(@Path("organizationId") String orgId,
                                                   @Path("serviceId") String serviceId,
                                                   @Path("version") String version
    );
}
