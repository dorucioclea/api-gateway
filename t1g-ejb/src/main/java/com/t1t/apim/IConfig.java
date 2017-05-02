package com.t1t.apim;

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
    String IDP_ENTITY_ID = "apiapp.idp.entity_id";
    String IDP_SAML_ENDPOINT = "apiapp.idp.saml_endpoint_url";
    String IDP_NAMEID_FORMAT = "apiapp.idp.nameid_format";
    String IDP_SCIM_ENDPOINT= "apiapp.idp.scim_endpoint_url";
    String IDP_SCIM_USER_LOGIN = "apiapp.idp.scim_user_login";
    String IDP_SCIM_USER_PWD = "apiapp.idp.scim_user_password";
    String IDP_OAUTH_TOKEN_ENDPOINT = "apiapp.idp.oauth_endpoint_url";
    String IDP_OAUTH_CLIENT_ID = "apiapp.idp.client_id";
    String IDP_OAUTH_CLIENT_SECRET = "apiapp.idp.client_secret";
    String IDP_SCIM_ACTIVATE = "apiapp.idp.enable_scim";
    String DATADOG_METRICS_URI = "apiapp.metrics.datadog.url";
    String DATADOG_METRICS_API_KEY = "apiapp.metrics.datadog.api_key";
    String DATADOG_METRICS_APPLICATION_KEY = "apiapp.metrics.datadog.application_key";
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
    String ANALYTICS_TOKEN = "apiapp.metrics.galileo.service_token";
    String ANALYTICS_ENVIRONMENT = "apiapp.metrics.galileo.environment";
    String ANALYTICS_RETRY_COUNT = "apiapp.metrics.galileo.retry_count";
    String ANALYTICS_QUEUE_SIZE = "apiapp.metrics.galileo.queue_size";
    String ANALYTICS_FLUSH_TIMEOUT = "apiapp.metrics.galileo.flush_timeout";
    String ANALYTICS_LOG_BODIES = "apiapp.metrics.galileo.log_bodies";
    String ANALYTICS_CONN_TIMEOUT = "apiapp.metrics.galileo.connection_timeout";
    String ANALYTICS_HOST = "apiapp.metrics.galileo.host";
    String ANALYTICS_PORT = "apiapp.metrics.galileo.port";
    String ANALYTICS_HTTPS = "apiapp.metrics.galileo.https";
    String ANALYTICS_HTTPS_VERIFY = "apiapp.metrics.galileo.https_verify";
    String NOTIFICATION_ENABLE_DEBUG = "apiapp.notifications.enable_debug";
    String NOTIFICATION_STARTUP_MAIL = "apiapp.notifications.startup_mail";
    String NOTIFICATION_MAIL_FROM = "apiapp.notifications.mail_from";
    String HYSTRIX_METRICS_TIMEOUT_VALUE = "apiapp.hystrix.metrics";
    String FILEPATH_LOCAL = "apiapp.filepaths.local";
    String IDP_NOTBEFORE_DELAY = "apiapp.idp.not_before_delay";
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
