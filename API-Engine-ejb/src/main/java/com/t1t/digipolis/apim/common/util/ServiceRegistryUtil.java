package com.t1t.digipolis.apim.common.util;

import java.util.*;

/**
 * Provides simple access to services.
 *
 */
public class ServiceRegistryUtil {

    private static Map<Class<?>, Set<?>> servicesCache = new HashMap<>();

    /**
     * Gets a single service by its interface.
     * @param serviceInterface the service interface
     * @throws IllegalStateException method has been invoked at an illegal or inappropriate time
     */
    @SuppressWarnings("javadoc")
    public static <T> T getSingleService(Class<T> serviceInterface) throws IllegalStateException {
        // Cached single service values are derived from the values cached when checking
        // for multiple services
        T rval = null;
        Set<T> services = getServices(serviceInterface);
        
        if (services.size() > 1) {
            throw new IllegalStateException("Multiple implementations found of " + serviceInterface); //$NON-NLS-1$
        } else if (!services.isEmpty()) {
            rval = services.iterator().next();
        }
        return rval;
    }

    /**
     * Get a set of service implementations for a given interface.
     * @param serviceInterface the service interface
     * @return the set of services
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> getServices(Class<T> serviceInterface) {
        synchronized(servicesCache) {
            if (servicesCache.containsKey(serviceInterface)) {
                return (Set<T>) servicesCache.get(serviceInterface);
            }
    
            Set<T> services = new LinkedHashSet<>();
            try {
                for (T service : ServiceLoader.load(serviceInterface)) {
                    services.add(service);
                }
            } catch (ServiceConfigurationError sce) {
                // No services found - don't check again.
            }
            servicesCache.put(serviceInterface, services);
            return services;
        }
    }

}
