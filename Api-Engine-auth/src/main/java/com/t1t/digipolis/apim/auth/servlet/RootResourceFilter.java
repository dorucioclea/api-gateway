package com.t1t.digipolis.apim.auth.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A simple filter that forwards the root REST call to /system/status
 *
 */
public class RootResourceFilter implements Filter {

    /**
     * Constructor.
     */
    public RootResourceFilter() {
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String path = httpReq.getPathInfo();
        if ("/".equals(path)) { //$NON-NLS-1$
            httpReq.getRequestDispatcher("/API-Engine-web/").forward(request, response); //$NON-NLS-1$
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
