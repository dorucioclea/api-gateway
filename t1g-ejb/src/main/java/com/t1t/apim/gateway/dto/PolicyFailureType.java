package com.t1t.apim.gateway.dto;

/**
 * In general, what kind of policy failure are we dealing with?
 *
 */
public enum PolicyFailureType {
    
    Authentication,
    Authorization,
    NotFound,
    Other

}
