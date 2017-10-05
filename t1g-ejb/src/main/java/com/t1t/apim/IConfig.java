package com.t1t.apim;

/**
 * Created by michallispashidis on 14/09/15.
 * Contains identifiers the services look for in the application.conf file.
 * Typesafe configuration concept.
 */
public interface IConfig {

    String PROP_FILE_DATE = "date";
    String PROP_FILE_VERSION = "version";
    String PROP_FILE_CONFIG_FILE = "configuration.file";

    String APP_ENVIRONMENT = "apiapp.environment";
    String KONG_URL = "apiapp.kong.endpoint_url";
    String KONG_HOST = "apiapp.kong.host";
    String KONG_URL_MANAGEMENT = "apiapp.kong.management_url";

    String DATADOG_METRICS_URI = "apiapp.metrics.datadog.url";
    String DATADOG_METRICS_API_KEY = "apiapp.metrics.datadog.api_key";
    String DATADOG_METRICS_APPLICATION_KEY = "apiapp.metrics.datadog.application_key";

    String SECURITY_REST_RESORUCES = "apiapp.security.rest_resources";
    String SECURITY_REST_AUTH_RESOURCES = "apiapp.security.rest_auth_resources";

    String NOTIFICATION_ENABLE_DEBUG = "apiapp.notifications.enable_debug";
    String NOTIFICATION_STARTUP_MAIL = "apiapp.notifications.startup_mail";
    String NOTIFICATION_MAIL_FROM = "apiapp.notifications.mail_from";
    String HYSTRIX_METRICS_TIMEOUT_VALUE = "apiapp.hystrix.metrics";
    String FILEPATH_LOCAL = "apiapp.filepaths.local";

    String GWD_APIENGINE_NAME = "apiapp.gateway_dependencies.apiengine.name";
    String GWD_APIENGINE_REQUEST_PATH = "apiapp.gateway_dependencies.apiengine.request_path";
    String GWD_APIENGINE_UPSTREAM_URL = "apiapp.gateway_dependencies.apiengine.upstream_url";
    String GWD_APIENGINE_STRIP_REQUEST_PATH = "apiapp.gateway_dependencies.apiengine.strip_request_path";
    String GWD_APIENGINEAUTH_NAME = "apiapp.gateway_dependencies.apiengineauth.name";
    String GWD_APIENGINEAUTH_REQUEST_PATH = "apiapp.gateway_dependencies.apiengineauth.request_path";
    String GWD_APIENGINEAUTH_UPSTREAM_URL = "apiapp.gateway_dependencies.apiengineauth.upstream_url";
    String GWD_APIENGINEAUTH_STRIP_REQUEST_PATH = "apiapp.gateway_dependencies.apiengineauth.strip_request_path";
    String GWD_GATEWAYKEYS_NAME = "apiapp.gateway_dependencies.gatewaykeys.name";
    String GWD_GATEWAYKEYS_REQUEST_PATH = "apiapp.gateway_dependencies.gatewaykeys.request_path";
    String GWD_GATEWAYKEYS_UPSTREAM_URL = "apiapp.gateway_dependencies.gatewaykeys.upstream_url";
    String GWD_GATEWAYKEYS_STRIP_REQUEST_PATH = "apiapp.gateway_dependencies.gatewaykeys.strip_request_path";
    String GWD_CLUSTER_INFO_NAME = "apiapp.gateway_dependencies.cluster_info.name";
    String GWD_CLUSTER_INFO_REQUEST_PATH = "apiapp.gateway_dependencies.cluster_info.request_path";
    String GWD_CLUSTER_INFO_UPSTREAM_URL = "apiapp.gateway_dependencies.cluster_info.upstream_url";
    String GWD_CLUSTER_INFO_STRIP_REQUEST_PATH = "apiapp.gateway_dependencies.cluster_info.strip_request_path";
}
