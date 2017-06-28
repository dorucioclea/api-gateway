package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MemberShipRequestFailedException extends AbstractUserException {

    public MemberShipRequestFailedException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ORG_IS_PRIVATE;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}