package com.t1t.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MailProviderNotFoundException extends AbstractNotFoundException {

    public MailProviderNotFoundException() {
    }

    public MailProviderNotFoundException(String message) {
        super(message);
    }

    public MailProviderNotFoundException(Throwable cause) {
        super(cause);
    }

    public MailProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MAIL_PROVIDER_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.MAIL_PROVIDER_NOT_FOUND_INFO;
    }
}