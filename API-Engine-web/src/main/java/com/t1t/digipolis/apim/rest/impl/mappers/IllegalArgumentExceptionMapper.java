package com.t1t.digipolis.apim.rest.impl.mappers;

import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.core.i18n.Messages;
import com.t1t.digipolis.apim.exceptions.ErrorCodes;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Provider
@ApplicationScoped
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Inject
    ISecurityContext securityContext;

    /**
     * Constructor.
     */
    public IllegalArgumentExceptionMapper() {
    }

    /**
     * @see ExceptionMapper#toResponse(Throwable)
     */
    @Override
    public Response toResponse(IllegalArgumentException data) {

        ErrorBean error = new ErrorBean();
        error.setMessage(data.getMessage());
        if (!StringUtils.isEmpty(data.getMessage())) {
            error.setMessage(data.getMessage());
        } else {
            error.setMessage(Messages.i18n.format("InvalidInput"));
        }
        error.setErrorCode(ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT);
        Response.ResponseBuilder builder = Response.status(ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT).header("X-ApiEngine-Error", "true");
        builder.type(MediaType.APPLICATION_JSON_TYPE);
        return builder.entity(error).build();
    }
}