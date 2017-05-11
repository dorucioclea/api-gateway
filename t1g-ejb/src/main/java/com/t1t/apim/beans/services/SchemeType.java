package com.t1t.apim.beans.services;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public enum SchemeType {
    HTTP("http"),
    HTTPS("https");

    private String scheme;

    SchemeType(String scheme) {
        this.scheme = scheme;
    }

    @JsonCreator
    public static SchemeType fromString(String scheme) {
        return scheme == null
                ? null
                : SchemeType.valueOf(scheme.toUpperCase());
    }

    @JsonValue
    public String getScheme() {
        return scheme;
    }
}
