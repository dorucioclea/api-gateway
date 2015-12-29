package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.cache.WebClientCacheBean;
import com.t1t.digipolis.apim.beans.user.UserSession;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by michallispashidis on 07/09/15.
 */
@Singleton
@Startup
@AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
public class CacheUtil implements Serializable {
    private static Logger _LOG = LoggerFactory.getLogger(CacheUtil.class.getName());

    @Resource(lookup = "java:jboss/infinispan/container/apiengine-cache")
    private EmbeddedCacheManager cacheManager;

    private Cache<String, WebClientCacheBean> ssoCache;
    private Cache<String, String> tokenCache;
    private Cache<String, UserSession> sessionCache;

    @PostConstruct
    public void setup() {
        _LOG.info("Caches found: {}",cacheManager.getCacheNames());
        ssoCache = cacheManager.getCache("sso");
        tokenCache = cacheManager.getCache("token");
        sessionCache = cacheManager.getCache("user");
        _LOG.info("Infinispan cache initialized.");
    }

    @Lock(LockType.READ)
    public  WebClientCacheBean getWebCacheBean(String webCacheId){
        final WebClientCacheBean webClientCacheBean = ssoCache.get(webCacheId);
        if (webClientCacheBean == null) {
            throw new SystemErrorException("SSO Cache with id " + webCacheId + " does not exist!");
        }
        return webClientCacheBean;
    }

    @Lock(LockType.READ)
    public  String getToken(String tokenId){
        final String cachedToken = tokenCache.get(tokenId);
        if (cachedToken == null) {
            throw new SystemErrorException("Token Cache with id " + tokenId + " does not exist!");
        }
        return cachedToken;
    }

    @Lock(LockType.READ)
    public  UserSession getSessionIndex(String userId){
        final UserSession session = sessionCache.get(userId);
        if (session == null) {
            throw new SystemErrorException("User session Cache with id " + userId + " does not exist!");
        }
        return session;
    }

    @Lock(LockType.WRITE)
    public void cacheWebClientCacheBean(String webCacheId, WebClientCacheBean bean){
        ssoCache.put(webCacheId, bean);
    }

    @Lock(LockType.WRITE)
    public void cacheToken(String tokenId, String token){
        tokenCache.put(tokenId, token);
    }

    @Lock(LockType.WRITE)
    public void cacheSessionIndex(String userId, UserSession userSession){
        sessionCache.put(userId, userSession);
    }

    @Lock(LockType.READ)
    public Set<String> getSSOKeys(){
        return ssoCache.keySet();
    }

    @Lock(LockType.READ)
    public Set<String> getTokenKeys(){
        return tokenCache.keySet();
    }

    @Lock(LockType.READ)
    public Set<String> getSessionKeys(){
        return sessionCache.keySet();
    }

    @Lock(LockType.READ)
    public Collection<WebClientCacheBean> getSSOCache(){
        return ssoCache.values();
    }

    @Lock(LockType.READ)
    public Collection<String> getTokens(){
        return tokenCache.values();
    }

    @Lock(LockType.READ)
    public Collection<UserSession> getSessionCache(){
        return sessionCache.values();
    }


}
