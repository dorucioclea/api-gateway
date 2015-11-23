package com.t1t.digipolis.apim.beans.jwt;

/**
 * Created by michallispashidis on 23/11/15.
 */
public interface IJWT {
    //JWT header
    String HEADER_TYPE = "typ";
    String HEADER_TYPE_VALUE = "JWT";
    //JWT keys
    String NAME = "name";
    String EMAIL = "email";
    String GIVEN_NAME = "givenname";
    String SURNAME = "surname";
}
