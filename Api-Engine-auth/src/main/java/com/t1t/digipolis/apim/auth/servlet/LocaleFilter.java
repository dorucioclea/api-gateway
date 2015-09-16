package com.t1t.digipolis.apim.auth.servlet;

import com.t1t.digipolis.apim.common.util.AbstractMessages;

import javax.servlet.*;
import java.io.IOException;
import java.util.Locale;

/**
 * A simple servlet filter that sets the user's locale on the i18n
 * messages framework so that messages logged are in the correct
 * locale.
 */
public class LocaleFilter implements Filter {

    /**
     * Constructor.
     */
    public LocaleFilter() {
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        Locale locale = request.getLocale();
        AbstractMessages.setLocale(locale);
        try {
            chain.doFilter(request, response);
        } finally {
            AbstractMessages.clearLocale();
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}