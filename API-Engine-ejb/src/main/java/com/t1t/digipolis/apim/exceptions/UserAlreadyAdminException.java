package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class UserAlreadyAdminException extends AbstractAlreadyExistsException {

    public UserAlreadyAdminException() {
        super();
    }

    public UserAlreadyAdminException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_ALREADY_ADMIN;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.USER_ALREADY_ADMIN_INFO;
    }
}