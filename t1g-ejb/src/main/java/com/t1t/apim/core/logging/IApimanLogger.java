package com.t1t.apim.core.logging;

/**
 * Simple logging interfaces. Later we'll add the ability to pass context info.
 *
 */
public interface IApimanLogger {

    /**
     * Log an info level message
     *
     * @param message the message
     */
    void info(String message);

    /**
     * Log a warning
     *
     * @param message the message
     */
    void warn(String message);

    /**
     * Log a debug level message
     *
     * @param message the message
     */
    void debug(String message);

    /**
     * Log a trace level message
     *
     * @param message the message
     */
    void trace(String message);

    /**
     * Log an error level message.
     *
     * @param error
     */
    void error(Throwable error);

    /**
     * Log an error level message.
     *
     * @param message
     * @param error
     */
    void error(String message, Throwable error);
}