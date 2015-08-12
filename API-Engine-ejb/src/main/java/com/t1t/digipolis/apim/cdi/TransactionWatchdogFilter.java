package com.t1t.digipolis.apim.cdi;

import com.t1t.digipolis.apim.jpa.AbstractJpaStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A servlet filter that ensures we don't have any transactions hanging around
 * once the request is complete.
 */
public class TransactionWatchdogFilter implements Filter {

    /**
     * Constructor.
     */
    public TransactionWatchdogFilter() {
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
        chain.doFilter(request, response);
        try {
            if (AbstractJpaStorage.isTxActive()) {
                throw new Exception("Error: storage transaction is still open for request: " + ((HttpServletRequest) request).getPathInfo()); //$NON-NLS-1$
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
