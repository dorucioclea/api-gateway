package com.t1t.digipolis.apim.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Default
public class EventService implements DefaultEventProvider {

    private static final Logger _LOG = LoggerFactory.getLogger(EventService.class);

}