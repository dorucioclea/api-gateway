package com.t1t.apim.idp;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IDPConstants {
    String MAIL_HOST = "host";
    String MAIL_PORT = "port";
    String MAIL_AUTH = "auth";
    String MAIL_USER = "user";
    String MAIL_PASSWORD = "password";
    String MAIL_FROM = "from";

    String KEYSTORE_PROVIDER_TYPE = "org.keycloak.keys.KeyProvider";
    String KEYSTORE_PROVIDER_ID = "java-keystore";
    String KEYSTORE_PASSWORD = "keystorePassword";
    String KEYSTORE_KEY_PASSWORD = "keyPassword";
    String KEYSTORE_ALIAS = "keyAlias";
    String KEYSTORE_PATH = "keystore";
    String KEYSTORE_ACTIVE = "active";
    String KEYSTORE_ENABLED = "enabled";
    String KEYSTORE_PRIORITY = "priority";

    String CLIENT_USER_INFO_RESPONSE_SIGNATURE_ALGORITHM = "user.info.response.signature.alg";
    String CLIENT_REQUEST_OBJECT_SIGNATURE_ALGORITHM = "request.object.signature.alg";
    String CLIENT_SIGNING_ALGORITHM_RS256 = "RS256";

    String OPENID_CONNECT = "openid-connect";

}