package com.t1t.apim.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by michallispashidis on 05/04/16.
 */
@WebListener
public class InitServlet implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //nothing to do
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //nothing to do
    }
}