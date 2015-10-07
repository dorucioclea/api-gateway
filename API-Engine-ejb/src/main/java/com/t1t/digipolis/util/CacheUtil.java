package com.t1t.digipolis.util;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by michallispashidis on 07/09/15.
 */
@ApplicationScoped
public class CacheUtil {
    //Clien application cache
    private final String CLIENT_CACHE = "clientcache";
    private CacheManager manager;

    {
        Configuration cacheManagerConfiguration = new Configuration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CLIENT_CACHE, 200)
                .eternal(true)
                .maxEntriesLocalHeap(200);
        cacheManagerConfiguration.addCache(cacheConfiguration);
        manager = new CacheManager(cacheManagerConfiguration);
    }

    public Ehcache getClientAppCache(){
        return manager.getEhcache(CLIENT_CACHE);
    }
}
