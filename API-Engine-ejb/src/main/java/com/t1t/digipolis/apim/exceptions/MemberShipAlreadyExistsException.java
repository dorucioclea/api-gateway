package com.t1t.digipolis.apim.exceptions;

import com.t1t.digipolis.apim.exceptions.AbstractAlreadyExistsException;
import com.t1t.digipolis.apim.exceptions.AlreadyExistsException;
import com.t1t.digipolis.apim.exceptions.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MemberShipAlreadyExistsException extends AbstractAlreadyExistsException {

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_ALREADY_A_MEMBER;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.USER_ALREADY_A_MEMBER_INFO;
    }
}