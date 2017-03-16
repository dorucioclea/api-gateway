package com.t1t.apim.core.logging;

/**
 * Particularly useful for testing purposes: provides time.
 *
 */
public interface Time {

    /**
     * @return the current time in ISO8601 format
     */
    String currentTimeIso8601();
}
