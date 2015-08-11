package com.t1t.digipolis.apim.rest.impl.util;

import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.resources.exceptions.InvalidNameException;
import org.apache.commons.lang.StringUtils;

/**
 * Validates the name of an entity.
 */
public class FieldValidator {

    /**
     * Validates an entity name.
     * @param name
     * @throws InvalidNameException
     */
    public static void validateName(String name) throws InvalidNameException {
        if (StringUtils.isEmpty(name)) {
            throw ExceptionFactory.invalidNameException(Messages.i18n.format("FieldValidator.EmptyNameError")); //$NON-NLS-1$
        }
    }

    /**
     * Validates an version.
     * @param name
     * @throws InvalidNameException
     */
    public static void validateVersion(String name) throws InvalidNameException {
        if (StringUtils.isEmpty(name)) {
            throw ExceptionFactory.invalidVersionException(Messages.i18n.format("FieldValidator.EmptyVersionError")); //$NON-NLS-1$
        }
    }

}
