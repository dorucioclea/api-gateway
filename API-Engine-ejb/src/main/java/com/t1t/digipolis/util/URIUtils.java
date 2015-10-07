package com.t1t.digipolis.util;

/**
 * Created by michallispashidis on 7/10/15.
 */
public class URIUtils {
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
}
