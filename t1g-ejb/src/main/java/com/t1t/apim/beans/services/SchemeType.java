package com.t1t.apim.beans.services;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public enum SchemeType {
    HTTP("http", 80),
    HTTPS("https", 443);

    private String scheme;
    private Integer port;

    SchemeType(String scheme, Integer port) {
        this.scheme = scheme;
        this.port = port;
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

    public Integer getPort() {
        return port;
    }

}
