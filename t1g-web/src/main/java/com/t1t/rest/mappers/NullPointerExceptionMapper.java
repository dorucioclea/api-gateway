package com.t1t.rest.mappers;

import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.i18n.Messages;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
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
public class NullPointerExceptionMapper extends AbstractExceptionMapper<NullPointerException> {
}