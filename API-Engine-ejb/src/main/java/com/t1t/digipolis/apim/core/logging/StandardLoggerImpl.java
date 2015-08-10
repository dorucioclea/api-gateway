package com.t1t.digipolis.apim.core.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simplest possible logger implementation, just does a straight pass-through.
 *
 */
public class StandardLoggerImpl implements IApimanDelegateLogger {

    private Logger delegatedLogger;

    @Override
    public IApimanLogger createLogger(String name) {
        delegatedLogger = LogManager.getLogger(name);
        return this;
    }

    @Override
    public IApimanLogger createLogger(Class <?> klazz) {
        delegatedLogger = LogManager.getLogger(klazz);
        return this;
    }

    @Override
    public void info(String message) {
        delegatedLogger.info(message);
    }

    @Override
    public void debug(String message) {
        delegatedLogger.debug(message);
    }

    @Override
    public void trace(String message) {
        delegatedLogger.trace(message);
    }

    @Override
    public void warn(String message) {
        delegatedLogger.warn(message);
    }

    @Override
    public void error(Throwable error) {
        delegatedLogger.error(error.getMessage(), error);
    }

    @Override
    public void error(String message, Throwable error) {
        delegatedLogger.error(message, error);
    }
}
