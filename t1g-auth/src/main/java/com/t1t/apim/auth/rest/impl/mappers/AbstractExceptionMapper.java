package com.t1t.apim.auth.rest.impl.mappers;

import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.i18n.Messages;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractExceptionMapper.class);

    /**
     * @see ExceptionMapper#toResponse(Throwable)
     */
    @Override
    public Response toResponse(final T data) {
        log.error("Mapped exception: ", data);
        ErrorBean error = new ErrorBean();
        error.setMessage(data.getMessage());
        if (!StringUtils.isEmpty(data.getMessage())) {
            error.setMessage(data.getMessage());
        } else {
            error.setMessage(Messages.i18n.format("InvalidInput"));
        }
        error.setErrorCode(ErrorCodes.ERROR_PRECONDITIONS_FAILED);
        Response.ResponseBuilder builder = Response.status(ErrorCodes.HTTP_STATUS_CODE_PRECONDITION_FAILED).header("X-ApiEngineAuth-Error", "true");
        builder.type(MediaType.APPLICATION_JSON_TYPE);
        return builder.entity(error).build();
    }

}