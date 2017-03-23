package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ApplicationOAuthInformationNotFoundException extends AbstractNotFoundException {

    public ApplicationOAuthInformationNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_OAUTH_INFO_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_OAUTH_INFO_NOT_FOUND_INFO;
    }
}