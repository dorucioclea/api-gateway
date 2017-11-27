
package com.t1t.kong.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public enum Metric {

    @SerializedName("request_count")
    REQUEST_COUNT("request_count"),
    @SerializedName("latency")
    LATENCY("latency"),
    @SerializedName("request_size")
    REQUEST_SIZE("request_size"),
    @SerializedName("status_count")
    STATUS_COUNT("status_count"),
    @SerializedName("response_size")
    RESPONSE_SIZE("response_size"),
    @SerializedName("unique_users")
    UNIQUE_USERS("unique_users"),
    @SerializedName("request_per_user")
    REQUEST_PER_USER("request_per_user"),
    @SerializedName("upstream_latency")
    UPSTREAM_LATENCY("upstream_latency");
    private final String value;
    private static Map<String, Metric> constants = new HashMap<String, Metric>();

    static {
        for (Metric c: values()) {
            constants.put(c.value, c);
        }
    }

    private Metric(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Metric fromValue(String value) {
        Metric constant = constants.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
