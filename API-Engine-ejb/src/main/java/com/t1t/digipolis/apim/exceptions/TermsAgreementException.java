package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class TermsAgreementException extends AbstractInvalidInputException {

    public TermsAgreementException() {
        super();
    }

    public TermsAgreementException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.TERMS_AGREEMENT_MISSING;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.TERMS_AGREEMENT_MISSING_INFO;
    }
}