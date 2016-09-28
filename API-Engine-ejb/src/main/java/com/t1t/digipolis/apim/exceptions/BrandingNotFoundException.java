package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class BrandingNotFoundException extends AbstractNotFoundException {

    public BrandingNotFoundException() {
        super();
    }

    public BrandingNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_BRANDING_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_BRANDING_NOT_FOUND_INFO;
    }
}