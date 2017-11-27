
package com.t1t.kong.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public enum Method_ {

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
    DELETE("DELETE");
    private final String value;
    private static Map<String, Method_> constants = new HashMap<String, Method_>();

    static {
        for (Method_ c: values()) {
            constants.put(c.value, c);
        }
    }

    private Method_(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Method_ fromValue(String value) {
        Method_ constant = constants.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
