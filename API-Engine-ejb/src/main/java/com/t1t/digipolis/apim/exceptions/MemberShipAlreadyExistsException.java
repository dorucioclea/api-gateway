package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MemberShipAlreadyExistsException extends AbstractAlreadyExistsException {

    public MemberShipAlreadyExistsException(String message) {
        super(message);
    }

    public MemberShipAlreadyExistsException() {
        super();
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_ALREADY_A_MEMBER;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.USER_ALREADY_A_MEMBER_INFO;
    }
}