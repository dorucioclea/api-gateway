package com.t1t.digipolis.apim;

/**
 * Created by michallispashidis on 14/09/15.
 * Contains identifiers the services look for in the application.conf file.
 * Typesafe configuration concept.
 */
public interface IConfig {
    String APP_VERSION = "apiapp.version";
    String APP_ENVIRONMENT = "apiapp.environment";
    String KONG_URL = "apiapp.kong.endpoint_url";
    String KONG_URL_MANAGEMENT = "apiapp.kong.management_url";
    String IDP_SAML_ENDPOINT = "apiapp.idp.saml_endpoint_url";
    String IDP_SCIM_ENDPOINT = "apiapp.idp.scim_endpoint_url";
    String METRICS_SCHEME = "apiapp.metrics.scheme";
    String METRICS_DNS = "apiapp.metrics.url";
    String METRICS_PORT = "apiapp.metrics.ports.default";
    String CONSENT_URI = "apiapp.oauth.consent_uri";
    String ENVIRONMENT = "apiapp.environment";
    String DEFAULT_USER_ORGANIZATION = "apiapp.defaults.orgId";
    String DEFAULT_USER_ROLES_FOR_DEFAULT_ORG = "apiapp.defaults.roles";
}
