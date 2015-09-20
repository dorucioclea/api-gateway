package com.t1t.digipolis.apim.security.impl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * An http filter that supports the {@link DefaultSecurityContext} implementation.
 *
 */
public class ApplicationAuthSecurityContextFilter implements Filter {

    /**
     * Constructor.
     */
    public ApplicationAuthSecurityContextFilter() {
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ApiEngineAppSecurityContext.setServletRequest((HttpServletRequest) request);
        try {
            chain.doFilter(request, response);
        } finally {
            ApiEngineAppSecurityContext.clearServletRequest();
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
