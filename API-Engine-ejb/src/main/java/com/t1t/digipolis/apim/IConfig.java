package com.t1t.digipolis.apim;

/**
 * Created by michallispashidis on 14/09/15.
 * Contains identifiers the services look for in the application.conf file.
 * Typesafe configuration concept.
 */
public interface IConfig {
    String METRICS_SCHEME = "apiapp.metrics.scheme";
    String METRICS_DNS = "apiapp.metrics.url";
    String METRICS_PORT = "apiapp.metrics.ports.default";
}
