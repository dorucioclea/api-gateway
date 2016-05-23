package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class CachingException extends AbstractSystemException {

    public CachingException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_FORBIDDEN;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.CACHING_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}