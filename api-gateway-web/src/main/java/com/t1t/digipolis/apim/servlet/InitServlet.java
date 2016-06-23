package com.t1t.digipolis.apim.servlet;

import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by michallispashidis on 05/04/16.
 */
@WebListener
public class InitServlet implements ServletContextListener {
    private static final Logger _LOG = LoggerFactory.getLogger(InitServlet.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //nothing to do
    }
}
