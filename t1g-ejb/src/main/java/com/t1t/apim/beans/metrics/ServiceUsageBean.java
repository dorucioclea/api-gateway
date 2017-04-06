package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceUsageBean implements Serializable {

    private JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServiceUsageBean{" +
                "data=" + data +
                '}';
    }
}