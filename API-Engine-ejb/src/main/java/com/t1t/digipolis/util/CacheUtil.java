package com.t1t.digipolis.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;

/**
 * Created by michallispashidis on 07/09/15.
 */
@Singleton
@Startup
public class CacheUtil implements Serializable {
    //Clien application cache
    private final static String CLIENT_CACHE = "clientcache";
    private final static String TOKEN_CACHE = "tokencache";
    private static CacheManager manager;

    static {
        Configuration cacheManagerConfiguration = new Configuration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CLIENT_CACHE, 200)
                .eternal(true)
                .maxEntriesLocalHeap(200);
        CacheConfiguration tokenConfiguration = new CacheConfiguration(TOKEN_CACHE, 500)
                .eternal(true)
                .maxEntriesLocalHeap(500);
        cacheManagerConfiguration.addCache(cacheConfiguration);
        cacheManagerConfiguration.addCache(tokenConfiguration);
        manager = new CacheManager(cacheManagerConfiguration);
    }

    public Ehcache getClientAppCache() {
        return manager.getEhcache(CLIENT_CACHE);
    }

    public Ehcache getUserTokenCache(){return manager.getEhcache(TOKEN_CACHE);}

    @PreDestroy
    public void shutdown() {
        manager.clearAll();
        manager.shutdown();
    }
}
