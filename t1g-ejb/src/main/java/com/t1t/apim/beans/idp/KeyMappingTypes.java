package com.t1t.apim.beans.idp;

/**
 * Created by michallispashidis on 30/06/16.
 * Available types for supported IDP protocols.
 * The types are typically used in the key_mapping table in order to provide key claim mapping between several protocols
 */
public enum KeyMappingTypes {
    SAML2, JWT
}
