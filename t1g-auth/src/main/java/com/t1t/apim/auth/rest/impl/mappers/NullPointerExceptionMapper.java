package com.t1t.apim.auth.rest.impl.mappers;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.Provider;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Provider
@ApplicationScoped
public class NullPointerExceptionMapper extends AbstractExceptionMapper<NullPointerException> {
}