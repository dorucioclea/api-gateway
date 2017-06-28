package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ContractRequestFailedException extends AbstractUserException {

    public ContractRequestFailedException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_ALREADY_EXISTS;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.CONTRACT_ALREADY_REQUESTED;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}