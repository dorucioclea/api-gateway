package com.t1t.digipolis.apim.security.impl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * An http filter that supports the {@link DefaultSecurityContext} implementation.
 *
 */
public class DefaultSecurityContextFilter implements Filter {
    
    /**
     * Constructor.
     */
    public DefaultSecurityContextFilter() {
    }
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }
    
    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        DefaultSecurityContext.setServletRequest((HttpServletRequest) request);
        try {
            chain.doFilter(request, response);
        } finally {
            DefaultSecurityContext.clearServletRequest();
            DefaultSecurityContext.clearPermissions();
        }
    }
    
    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
