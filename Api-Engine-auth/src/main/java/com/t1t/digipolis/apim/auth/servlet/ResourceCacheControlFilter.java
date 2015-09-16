package com.t1t.digipolis.apim.auth.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * {@link Filter} to add cache control headers for resources such as CSS and images.
 */
public class ResourceCacheControlFilter implements Filter {

    /**
     * C'tor
     */
    public ResourceCacheControlFilter() {
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
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String v = request.getParameter("v"); //$NON-NLS-1$
        if (v == null)
            v = ""; //$NON-NLS-1$

        // By default, cache aggressively.  However, if a file contains '.nocache.' as part of its
        // name, then tell the browser not to cache it.  Also, if the version of the resource being
        // requested contains 'SNAPSHOT' then don't cache.
        if (requestURI.contains(".nocache.") //$NON-NLS-1$
                || v.contains("SNAPSHOT") //$NON-NLS-1$
                || "true".equals(System.getProperty("apiman.resource-caching.disabled", "false"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        {
            Date now = new Date();
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setDateHeader("Date", now.getTime()); //$NON-NLS-1$
            // one day old
            httpResponse.setDateHeader("Expires", now.getTime() - 86400000L); //$NON-NLS-1$
            httpResponse.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
            httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            Date now = new Date();
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setDateHeader("Date", now.getTime()); //$NON-NLS-1$
            // Expire in one year
            httpResponse.setDateHeader("Expires", now.getTime() + 31536000000L); //$NON-NLS-1$
            // Cache for one year
            httpResponse.setHeader("Cache-control", "public, max-age=31536000"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }
}
