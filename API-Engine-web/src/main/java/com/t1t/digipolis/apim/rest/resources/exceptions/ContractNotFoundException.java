package com.t1t.digipolis.apim.rest.resources.exceptions;



/**
 * Thrown when trying to get, update, or delete a contract that does not exist.
 *
 */
public class ContractNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -8321449288246652304L;

    /**
     * Constructor.
     */
    public ContractNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public ContractNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.CONTRACT_NOT_FOUND;
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.CONTRACT_NOT_FOUND_INFO;
    }

}
