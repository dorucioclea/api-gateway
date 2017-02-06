package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class BrandingCannotBeDeletedException extends AbstractRestException {

    public BrandingCannotBeDeletedException() {
        super();
    }

    public BrandingCannotBeDeletedException(String message) {
        super(message);
    }

    public BrandingCannotBeDeletedException(Throwable cause) {
        super(cause);
    }

    public BrandingCannotBeDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_BRANDING_CANNOT_BE_DELETED;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_BRANDING_CANNOT_BE_DELETED_INFO;
    }
}