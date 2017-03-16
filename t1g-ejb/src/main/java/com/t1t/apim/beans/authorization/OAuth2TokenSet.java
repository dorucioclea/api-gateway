package com.t1t.apim.beans.authorization;

import com.t1t.apim.beans.pagination.AbstractPaginationBean;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class OAuth2TokenSet extends AbstractPaginationBean {

    private Set<OAuth2Token> data;


}