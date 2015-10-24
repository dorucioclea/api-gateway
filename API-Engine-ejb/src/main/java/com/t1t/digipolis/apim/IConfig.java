package com.t1t.digipolis.apim;

/**
 * Created by michallispashidis on 14/09/15.
 * Contains identifiers the services look for in the application.conf file.
 * Typesafe configuration concept.
 */
public interface IConfig {
    String APP_ENVIRONMENT = "apiapp.environment";
    String KONG_URL = "apiapp.kong.endpoint_url";
    String KONG_URL_MANAGEMENT = "apiapp.kong.management_url";
    String IDP_SAML_ENDPOINT = "apiapp.idp.saml_endpoint_url";
    String IDP_NAMEID_FORMAT = "apiapp.idp.nameid_format";
    String IDP_SCIM_ENDPOINT = "apiapp.idp.scim_endpoint_url";
    String IDP_OAUTH_TOKEN_ENDPOINT = "apiapp.idp.oauth_endpoint_url";
    String IDP_OAUTH_CLIENT_ID = "apiapp.idp.client_id";
    String IDP_OAUTH_CLIENT_SECRET = "apiapp.idp.client_secret";
    String METRICS_SCHEME = "apiapp.metrics.scheme";
    String METRICS_DNS = "apiapp.metrics.url";
    String METRICS_PORT = "apiapp.metrics.ports.default";
    String CONSENT_URI = "apiapp.oauth.consent_uri";
    String DEFAULT_USER_ORGANIZATION = "apiapp.defaults.orgId";
    String DEFAULT_USER_ROLES_FOR_DEFAULT_ORG = "apiapp.defaults.roles";
    String SECURITY_REST_RESORUCES = "rest_resources";
    String SECURITY_REST_AUTH_RESOURCES = "rest_auth_resources";
    String PROP_FILE_DATE = "date";
    String PROP_FILE_VERSION = "version";
    String PROP_FILE_CONFIG_FILE = "configuration.file";
}
