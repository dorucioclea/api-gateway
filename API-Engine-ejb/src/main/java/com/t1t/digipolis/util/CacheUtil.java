package com.t1t.digipolis.util;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

/**
 * Created by michallispashidis on 07/09/15.
 */
public class CacheUtil {
    //Clien application cache
    private static final String CLIENT_CACHE = "clientcache";
    private static CacheManager manager;

    static{
        Configuration cacheManagerConfiguration = new Configuration();
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CLIENT_CACHE, 20)
                .eternal(true)
                .maxEntriesLocalHeap(20);
        cacheManagerConfiguration.addCache(cacheConfiguration);
        manager = new CacheManager(cacheManagerConfiguration);
    }

    public static Ehcache getClientAppCache(){
        Ehcache cache = manager.getEhcache(CLIENT_CACHE);
        return cache;
    }
}
