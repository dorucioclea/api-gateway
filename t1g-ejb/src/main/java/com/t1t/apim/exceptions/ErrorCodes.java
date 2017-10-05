package com.t1t.apim.exceptions;


/**
 * A set of error codes used by the application when returning errors via
 * the DT REST API.
 *
 */
public final class ErrorCodes {

    //
    // HTTP status codes
    //
    public static final int HTTP_STATUS_CODE_INVALID_INPUT  = 400;
    public static final int HTTP_STATUS_CODE_UNAUTHORIZED   = 401;
    public static final int HTTP_STATUS_CODE_FORBIDDEN      = 403;
    public static final int HTTP_STATUS_CODE_NOT_FOUND      = 404;
    public static final int HTTP_STATUS_CODE_ALREADY_EXISTS = 409;
    public static final int HTTP_STATUS_CODE_INVALID_STATE  = 409;
    public static final int HTTP_STATUS_CODE_SYSTEM_ERROR   = 500;
    public static final int HTTP_STATUS_CODE_UNAVAILABLE    = 503;
    public static final int HTTP_STATUS_CODE_NETWORK_CONNECT_TIMEOUT_ERROR   = 599;

    public static final String HTTP_STATUS_CODE_INVALID_INPUT_INFO = null;
    public static final String HTTP_STATUS_CODE_UNAVAILABLE_INFO = null;


    //
    // User API related
    //
    public static final int USER_NOT_FOUND                  = 1001;
    public static final int USER_ALREADY_EXISTS             = 1002;
    public static final int USER_STILL_OWNER                = 1003;
    public static final int USER_ALREADY_A_MEMBER           = 1004;
    public static final int JWT_INVALID                     = 1005;
    public static final int USER_ALREADY_ADMIN              = 1006;
    public static final int INVALID_SAML                    = 1007;

    public static final String USER_NOT_FOUND_INFO          = null;
    public static final String USER_ALREADY_EXISTS_INFO     = null;
    public static final String USER_STILL_OWNER_INFO        = null;
    public static final String USER_ALREADY_A_MEMBER_INFO   = null;
    public static final String JWT_INVALID_INFO             = null;
    public static final String USER_ALREADY_ADMIN_INFO      = null;
    public static final String INVALID_SAML_INFO            = null;


    //
    // Role API related
    //
    public static final int ROLE_NOT_FOUND                  = 2001;
    public static final int ROLE_ALREADY_EXISTS             = 2002;

    public static final String ROLE_NOT_FOUND_INFO          = null;
    public static final String ROLE_ALREADY_EXISTS_INFO     = null;


    //
    // Organization API related
    //
    public static final int ORG_ALREADY_EXISTS              = 3001;
    public static final int ORG_NOT_FOUND                   = 3002;
    public static final int ORG_IS_PRIVATE                  = 3003;
    public static final int ORG_CANNOT_BE_DELETED           = 3004;

    public static final String ORG_ALREADY_EXISTS_INFO      = null;
    public static final String ORG_NOT_FOUND_INFO           = null;
    public static final String ORG_IS_PRIVATE_INFO          = null;
    public static final String ORG_CANNOT_BE_DELETED_INFO   = null;


    //
    // Application API related
    //
    public static final int APP_ALREADY_EXISTS              = 4001;
    public static final int APP_NOT_FOUND                   = 4002;
    public static final int APP_VERSION_NOT_FOUND           = 4003;
    public static final int CONTRACT_NOT_FOUND              = 4004;
    public static final int CONTRACT_ALREADY_EXISTS         = 4005;
    public static final int APP_STATUS_ERROR                = 4006;
    public static final int APP_VERSION_ALREADY_EXISTS      = 4007;
    public static final int CONTRACT_ALREADY_REQUESTED      = 4008;
    public static final int TERMS_AGREEMENT_MISSING         = 4009;
    public static final int APP_OAUTH_INFO_NOT_FOUND        = 4010;

    public static final String APP_ALREADY_EXISTS_INFO      = null;
    public static final String APP_NOT_FOUND_INFO           = null;
    public static final String APP_VERSION_NOT_FOUND_INFO   = null;
    public static final String CONTRACT_NOT_FOUND_INFO      = null;
    public static final String CONTRACT_ALREADY_EXISTS_INFO = null;
    public static final String APP_STATUS_ERROR_INFO        = null;
    public static final String APP_VERSION_ALREADY_EXISTS_INFO = null;
    public static final String TERMS_AGREEMENT_MISSING_INFO = null;
    public static final String APP_OAUTH_INFO_NOT_FOUND_INFO= null;



    //
    // Service API related
    //
    public static final int SERVICE_ALREADY_EXISTS              = 5001;
    public static final int SERVICE_NOT_FOUND                   = 5002;
    public static final int SERVICE_VERSION_NOT_FOUND           = 5003;
    public static final int SERVICE_STATUS_ERROR                = 5004;
    public static final int SERVICE_DEFINITION_NOT_FOUND        = 5005;
    public static final int SERVICE_VERSION_ALREADY_EXISTS      = 5006;
    public static final int SERVICE_STILL_HAS_CONTRACTS         = 5009;
    public static final int SERVICE_BASEPATH_ALREADY_EXISTS     = 5010;
    public static final int SERVICE_UPDATE_VALUES_INVALID       = 5011;
    public static final int SERVICE_VERSION_NOT_AVAILABLE       = 5012;
    public static final int SERVICE_LOAD_BALANCING_INVALID      = 5013;

    public static final String SERVICE_ALREADY_EXISTS_INFO      = null;
    public static final String SERVICE_NOT_FOUND_INFO           = null;
    public static final String SERVICE_VERSION_NOT_FOUND_INFO   = null;
    public static final String SERVICE_STATUS_ERROR_INFO        = null;
    public static final String SERVICE_DEFINITION_NOT_FOUND_INFO   = null;
    public static final String SERVICE_VERSION_ALREADY_EXISTS_INFO      = null;
    public static final String SERVICE_UPDATE_VALUES_INVALID_INFO   = null;
    public static final String SERVICE_VERSION_NOT_AVAILABLE_INFO = null;
    public static final String SERVICE_LOAD_BALANCING_INVALID_INFO  = null;

    //
    // Plan API related
    //
    public static final int PLAN_ALREADY_EXISTS              = 6001;
    public static final int PLAN_NOT_FOUND                   = 6002;
    public static final int PLAN_VERSION_NOT_FOUND           = 6003;
    public static final int PLAN_VERSION_ALREADY_EXISTS      = 6004;
    public static final int PLAN_CANNOT_BE_DELETED           = 6005;

    public static final String PLAN_ALREADY_EXISTS_INFO      = null;
    public static final String PLAN_NOT_FOUND_INFO           = null;
    public static final String PLAN_VERSION_NOT_FOUND_INFO   = null;
    public static final String PLAN_VERSION_ALREADY_EXISTS_INFO = null;
    public static final String PLAN_CANNOT_BE_DELETED_INFO   = null;

    //
    // Member API related
    //
    public static final int MEMBER_NOT_FOUND                  = 7001;

    public static final String MEMBER_NOT_FOUND_INFO          = null;



    //
    // Action API related
    //
    public static final int ACTION_ERROR                      = 8001;
    public static final String ACTION_ERROR_INFO              = null;


    //
    // Policy related
    //
    public static final int POLICY_INVALID                    = 9000;
    public static final int POLICY_NOT_FOUND                  = 9001;

    public static final String POLICY_NOT_FOUND_INFO          = null;


    //
    // Policy Definition related
    //
    public static final int POLICY_DEF_ALREADY_EXISTS              = 10001;
    public static final int POLICY_DEF_NOT_FOUND                   = 10002;
    public static final int POLICY_DEF_INVALID                     = 10003;

    public static final String POLICY_DEF_ALREADY_EXISTS_INFO      = null;
    public static final String POLICY_DEF_NOT_FOUND_INFO           = null;
    public static final String POLICY_DEF_INVALID_INFO             = null;


    //
    // Gateway related
    //
    public static final int GATEWAY_ALREADY_EXISTS              = 11001;
    public static final int GATEWAY_NOT_FOUND                   = 11002;
    public static final int GATEWAY_API_KEY_ALREADY_EXISTS      = 11003;

    public static final String GATEWAY_ALREADY_EXISTS_INFO      = null;
    public static final String GATEWAY_NOT_FOUND_INFO           = null;


    //
    // Plugin related
    //
    public static final int PLUGIN_ALREADY_EXISTS              = 12001;
    public static final int PLUGIN_NOT_FOUND                   = 12002;
    public static final int PLUGIN_RESOURCE_NOT_FOUND          = 12003;

    public static final String PLUGIN_ALREADY_EXISTS_INFO      = null;
    public static final String PLUGIN_NOT_FOUND_INFO           = null;
    public static final String PLUGIN_RESOURCE_NOT_FOUND_INFO  = null;


    //
    // Metrics related
    //
    public static final int METRIC_CRITERIA_INVALID            = 13001;
    public static final int METRIC_UNAVAILABLE                 = 13003;

    public static final String METRIC_CRITERIA_INVALID_INFO    = null;


    //
    // General cross-cutting errors
    //
    public static final int SEARCH_CRITERIA_INVALID         = 14001;
    public static final int NAME_INVALID                    = 14002;
    public static final int VERSION_INVALID                 = 14003;

    public static final String SEARCH_CRITERIA_INVALID_INFO = null;
    public static final String NAME_INVALID_INFO            = null;
    public static final String VERSION_INVALID_INFO         = null;

    //
    // Scope errors
    //

    public static final int SCOPE_NOT_FOUND                 = 15002;

    //
    // Event errors
    //

    public static final int EVENT_ERROR                     = 16001;
    public static final int EVENT_NOT_FOUND_ERROR           = 16004;
    public static final int EVENT_INVALID_STATUS_ERROR      = 16006;

    //
    // Cache related
    //

    public static final int CACHING_ERROR             = 17001;

    //
    // Security related
    //
    public static final int JWT_PUB_KEY_ERROR               = 18001;
    public static final String JWT_PUB_KEY_INFO             = null;

    //
    // Branding related
    //

    public static final int SERVICE_BRANDING_NOT_FOUND          = 19001;
    public static final int SERVICE_BRANDING_ALREADY_EXISTS     = 19002;
    public static final int SERVICE_BRANDING_CANNOT_BE_DELETED  = 19003;


    public static final String SERVICE_BRANDING_NOT_FOUND_INFO  = null;
    public static final String SERVICE_BRANDING_ALREADY_EXISTS_INFO  = null;
    public static final String SERVICE_BRANDING_CANNOT_BE_DELETED_INFO  = null;

    //
    // System related
    //

    public static final int SYSTEM_MAINTENANCE                  = 20001;
    public static final int SYSTEM_MAINTENANCE_LOGIN            = 20002;
    public static final int ENCRYPTION_ERROR                    = 20003;

    public static final String SYSTEM_MAINTENANCE_INFO          = null;

    //
    // IDP
    //

    public static final int UNAUTHORIZED_ISSUER                 = 21001;

    public static final String UNAUTHORIZED_ISSUER_INFO              = null;

    //
    // Error message keys
    //

    public static final String REQUEST_NULL = "nullValue";
    public static final String UNAUTHORIZED_ISSUER_KEY = "unauthorizedIssuer";
    public static final String ERROR_LOADING_PERMISSIONS        = "errorLoadingPermissions";
    public static final String CONFIG_PROPERTY_MISSING          = "configPropertyMissing";
    public static final String GATEWAY_NOT_RUNNING              = "gatewayNotRunning";
}
