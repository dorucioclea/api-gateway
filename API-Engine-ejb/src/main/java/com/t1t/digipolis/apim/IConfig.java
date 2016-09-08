package com.t1t.digipolis.apim;

/**
 * Created by michallispashidis on 14/09/15.
 * Contains identifiers the services look for in the application.conf file.
 * Typesafe configuration concept.
 */
public interface IConfig {
    String APP_ENVIRONMENT = "apiapp.environment";
    String KONG_URL = "apiapp.kong.endpoint_url";
    String KONG_HOST = "apiapp.kong.host";
    String KONG_URL_MANAGEMENT = "apiapp.kong.management_url";
    String IDP_SAML_ENDPOINT = "apiapp.idp.saml_endpoint_url";
    String IDP_NAMEID_FORMAT = "apiapp.idp.nameid_format";
    String METRICS_SCHEME = "apiapp.metrics.mongo.scheme";
    String METRICS_DNS = "apiapp.metrics.mongo.url";
    String METRICS_PORT = "apiapp.metrics.mongo.ports.default";
    String CONSENT_URI = "apiapp.oauth.consent_uri";
    String OAUTH_ENABLE_GTW_ENDPOINTS = "apiapp.oauth.enable_shared_endpoints";
    String DEFAULT_USER_ORGANIZATION = "apiapp.defaults.orgId";
    String DEFAULT_USER_ROLES_FOR_DEFAULT_ORG = "apiapp.defaults.roles";
    String SECURITY_REST_RESORUCES = "apiapp.security.rest_resources";
    String SECURITY_REST_AUTH_RESOURCES = "apiapp.security.rest_auth_resources";
    String PROP_FILE_DATE = "date";
    String PROP_FILE_VERSION = "version";
    String PROP_FILE_CONFIG_FILE = "configuration.file";
    String JWT_DEFAULT_TOKEN_EXP = "apiapp.jwt.token_default_exp_minutes";
    String NOTIFICATION_ENABLE_DEBUG = "apiapp.notifications.enable_debug";
    String NOTIFICATION_STARTUP_MAIL = "apiapp.notifications.startup_mail";
    String NOTIFICATION_MAIL_FROM = "apiapp.notifications.mail_from";
    String HYSTRIX_METRICS_TIMEOUT_VALUE = "apiapp.hystrix.metrics";
    String FILEPATH_LOCAL = "apiapp.filepaths.local";
}
