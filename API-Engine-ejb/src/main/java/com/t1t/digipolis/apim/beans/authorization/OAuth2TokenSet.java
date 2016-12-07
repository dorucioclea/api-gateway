package com.t1t.digipolis.apim.beans.authorization;

import com.t1t.digipolis.apim.beans.pagination.AbstractPaginationBean;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class OAuth2TokenSet extends AbstractPaginationBean {

    private Set<OAuth2Token> data;


}