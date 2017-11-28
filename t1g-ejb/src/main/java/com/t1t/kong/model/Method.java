
package com.t1t.kong.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public enum Method {

    @SerializedName("HEAD")
    HEAD("HEAD"),
    @SerializedName("GET")
    GET("GET"),
    @SerializedName("POST")
    POST("POST"),
    @SerializedName("PUT")
    PUT("PUT"),
    @SerializedName("PATCH")
    PATCH("PATCH"),
    @SerializedName("DELETE")
    DELETE("DELETE"),
    @SerializedName("OPTIONS")
    OPTIONS("OPTIONS"),
    @SerializedName("CONNECT")
    CONNECT("CONNECT"),
    @SerializedName("TRACE")
    TRACE("TRACE");
    private final String value;
    private static Map<String, Method> constants = new HashMap<String, Method>();

    static {
        for (Method c: values()) {
            constants.put(c.value, c);
        }
    }

    private Method(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Method fromValue(String value) {
        Method constant = constants.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
