package com.t1t.apim.auth.rest.impl.mappers;

import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.exceptions.AbstractRestException;
import com.t1t.apim.security.ISecurityContext;
import org.apache.commons.io.output.StringBuilderWriter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;

/**
 * Provider that maps an error.
 */
@Provider
@ApplicationScoped
public class RestExceptionMapper implements ExceptionMapper<AbstractRestException> {

    /**
     * @see ExceptionMapper#toResponse(Throwable)
     */
    @Override
    public Response toResponse(AbstractRestException data) {
        //String origin = securityContext.getRequestHeader("Origin"); //$NON-NLS-1$
        ErrorBean error = new ErrorBean();
        error.setType(data.getClass().getSimpleName());
        error.setErrorCode(data.getErrorCode());
        error.setMessage(data.getMessage());
        error.setMoreInfoUrl(data.getMoreInfoUrl());
        //TODO removed showing the stacktrace
        //error.setStacktrace(getStackTrace(data));
        ResponseBuilder builder = Response.status(data.getHttpCode()).header("X-ApiEngine-Error", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        // If CORS is being used, make sure to add X-Apiman-Error to the exposed headers
/*        if (origin != null) {
            builder = builder.header("Access-Control-Expose-Headers", "X-ApiEngine-Error") //$NON-NLS-1$ //$NON-NLS-2$
                    .header("Access-Control-Allow-Origin", origin) //$NON-NLS-1$
                    .header("Access-Control-Allow-Credentials", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }*/
        builder.type(MediaType.APPLICATION_JSON_TYPE);
        return builder.entity(error).build();
    }
}
