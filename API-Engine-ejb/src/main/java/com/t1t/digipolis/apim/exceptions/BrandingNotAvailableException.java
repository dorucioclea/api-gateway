package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class BrandingNotAvailableException extends AbstractAlreadyExistsException {

    public BrandingNotAvailableException() {
        super();
    }

    public BrandingNotAvailableException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_BRANDING_ALREADY_EXISTS;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_BRANDING_ALREADY_EXISTS_INFO;
    }
}