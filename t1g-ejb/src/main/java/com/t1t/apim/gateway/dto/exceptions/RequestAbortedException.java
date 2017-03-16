package com.t1t.apim.gateway.dto.exceptions;

import java.io.IOException;

/**
 * Thrown when a request is aborted, for example due to an {@link IOException}
 * when reading data from the client.
 *
 */
public class RequestAbortedException extends AbstractEngineException {

    private static final long serialVersionUID = -3731417524954193955L;

    /**
     * Constructor.
     */
    public RequestAbortedException() {
    }

}
