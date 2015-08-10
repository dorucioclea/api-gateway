package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to create an Contract that already exists.
 *
 */
public class ContractAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -2323774165040555437L;

    /**
     * Constructor.
     */
    public ContractAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public ContractAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.CONTRACT_ALREADY_EXISTS;
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.CONTRACT_ALREADY_EXISTS_INFO;
    }

}
