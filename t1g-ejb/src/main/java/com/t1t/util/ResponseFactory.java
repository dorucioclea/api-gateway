package com.t1t.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ResponseFactory {

    public static Response buildResponse(Response.Status httpCode, String headerName, String headerValue, Object entity) {
        Response.ResponseBuilder builder = Response.status(httpCode.getStatusCode());
        if (StringUtils.isNotEmpty(headerName) && StringUtils.isNotEmpty(headerValue)) {
            builder.header(headerName, headerValue);
        }
        if (entity != null) {
            builder.entity(entity);
        }
        return builder.type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Response buildResponse(Response.Status httpCode) {
        return buildResponse(httpCode, null, null, null);
    }

    public static Response buildResponse(Response.Status httpCode, Object entity) {
        return buildResponse(httpCode, null, null, entity);
    }

}