package com.t1t.util;

import com.t1t.apim.beans.services.SchemeType;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by michallispashidis on 7/10/15.
 */
public class URIUtils {

    private static final String HTTP = "http://";
    private static final String SCHEME_SEPARATOR = "://";
    private static final String PORT_SEPARATOR = ":";

    public static String setVirtualHost(String endpoint, String virtualHost) {
        return setVirtualHost(endpoint, virtualHost, true);
    }

    private static String setVirtualHost(String endpoint, String virtualHost, boolean retry) {
        String uriString = endpoint;
        try {
            URI uri = new URL(uriString).toURI();
            return new URI(uri.getScheme(), virtualHost, uri.getPath(), uri.getFragment()).toString();
        } catch (URISyntaxException | MalformedURLException e) {
            if (retry && !uriString.toLowerCase().contains(SCHEME_SEPARATOR)) {
                uriString = HTTP + uriString;
                return setVirtualHost(uriString, virtualHost, false);
            }
            return null;
        }
    }

    public static String uriFinalslashAppender(String uri) {
        if (!uri.endsWith("/")) return uri + "/";
        else return uri;
    }

    public static String uriFinalslashRemover(String uri) {
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        } else return uri;
    }

    public static String uriLeadingSlashRemover(String uri) {
        if (uri.startsWith("/")) {
            return uri.substring(1);
        } else return uri;
    }

    public static String uriLeadingSlashPrepender(String uri) {
        if (!uri.startsWith("/")) return "/" + uri;
        else return uri;
    }

    public static String buildEndpoint(SchemeType scheme, String host, Long port, String path) {
        try {
            return new URI(scheme.getScheme(), null, host, port == null ? -1 : port.intValue(), path, null, null).toString();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String appendPort(String dns, Long port) {
        if (StringUtils.isNotEmpty(dns)) {
            StringBuilder rval = new StringBuilder(dns);
            if (port != null) {
                rval.append(PORT_SEPARATOR).append(port);
            }
            return rval.toString();
        } else {
            return null;
        }
    }
}
