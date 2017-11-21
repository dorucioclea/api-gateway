package com.t1t.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 11/09/15.
 */
public enum ClientTokeType implements Serializable {
    opaque, saml2bearer, jwt
}
