package com.t1t.apim.beans.jwt;

/**
 * Created by michallispashidis on 23/11/15.
 */
public interface IJWT {
    //JWT header
    String HEADER_TYPE = "typ";
    String HEADER_TYPE_VALUE = "JWT";
    String HEADER_X5U = "x5u";
    String ISSUER_CLAIM = "iss";
    String EXPIRATION_CLAIM = "exp";
    //JWT keys
    String NAME = "name";
    String EMAIL = "email";
    String GIVEN_NAME = "given_name";
    String SURNAME = "family_name";
    String SERVICE_ACCOUNT = "isServiceAccount";
    String IMPERSONATE_USER = "impersonateUser";
}
