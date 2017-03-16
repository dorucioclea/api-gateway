package com.t1t.apim.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
