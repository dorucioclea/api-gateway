package com.t1t.util;

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

    public static String uriBackslashAppender(String uri){
        if(!uri.endsWith("/"))return uri+"/";
        else return uri;
    }

    public static String uriBackslashRemover(String uri){
        if(uri.endsWith("/")){
            return uri.substring(0,uri.length()-1);
        }
        else return uri;
    }

    public static String replaceHost(String uri, String newHost) {
        StringBuilder rval = new StringBuilder();
        if (uri.toLowerCase().startsWith("https://") || uri.toLowerCase().startsWith("http://")) {

        }
        else {

        }
        return rval.toString();
    }
}
