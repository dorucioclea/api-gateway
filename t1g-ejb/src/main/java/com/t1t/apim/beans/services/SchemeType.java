package com.t1t.apim.beans.services;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public enum SchemeType {
    HTTP("http"), HTTPS("https");

    private String scheme;

    SchemeType(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
