package com.t1t.digipolis.apim.core.config;

import com.t1t.digipolis.apim.common.config.ConfigFactory;
import org.apache.commons.configuration.Configuration;

import javax.enterprise.context.ApplicationScoped;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Configuration object for the API Manager.
 *
 */
@ApplicationScoped
public class ApiManagerConfig {

    public static final String APIMAN_MANAGER_CONFIG_LOGGER = "apiman-manager.config.logger"; //$NON-NLS-1$

    /* -------------------------------------------------------
     * Storage
     * ------------------------------------------------------- */
    public static final String APIMAN_MANAGER_STORAGE_TYPE = "apiman-manager.storage.type"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_STORAGE_ES_PROTOCOL = "apiman-manager.storage.es.protocol"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_STORAGE_ES_HOST = "apiman-manager.storage.es.host"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_STORAGE_ES_PORT = "apiman-manager.storage.es.port"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_STORAGE_ES_CLUSTER_NAME = "apiman-manager.storage.es.cluster-name"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_STORAGE_ES_INITIALIZE = "apiman-manager.storage.es.initialize"; //$NON-NLS-1$

    public static final String APIMAN_MANAGER_STORAGE_QUERY_TYPE = "apiman-manager.storage-query.type"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_IDM_STORAGE_TYPE = "apiman-manager.idm-storage.type"; //$NON-NLS-1$

    /* -------------------------------------------------------
     * Metrics
     * ------------------------------------------------------- */
    public static final String APIMAN_MANAGER_METRICS_TYPE = "apiman-manager.metrics.type"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_METRICS_ES_PROTOCOL = "apiman-manager.metrics.es.protocol"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_METRICS_ES_HOST = "apiman-manager.metrics.es.host"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_METRICS_ES_PORT = "apiman-manager.metrics.es.port"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_METRICS_ES_CLUSTER_NAME = "apiman-manager.metrics.es.cluster-name"; //$NON-NLS-1$

    public static final String APIMAN_MANAGER_SECURITY_CONTEXT_TYPE = "apiman-manager.security-context.type"; //$NON-NLS-1$

    public static final String APIMAN_PLUGIN_REPOSITORIES = "apiman.plugins.repositories"; //$NON-NLS-1$

    public static final String DEFAULT_ES_CLUSTER_NAME = "apiman"; //$NON-NLS-1$

    private final Configuration config;

    /**
     * Constructor.
     */
    public ApiManagerConfig() {
        config = loadProperties();
    }

    /**
     * Loads the config properties.
     */
    protected Configuration loadProperties() {
        return ConfigFactory.createConfig();
    }

    /**
     * @return the configured plugin repositories
     */
    public Set<URL> getPluginRepositories() {
        Set<URL> rval = new HashSet<>();
        String repositories = config.getString(APIMAN_PLUGIN_REPOSITORIES);
        if (repositories != null) {
            String[] split = repositories.split(","); //$NON-NLS-1$
            for (String repository : split) {
                try {
                    rval.add(new URL(repository.trim()));
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return rval;
    }

    public String getSecurityContextType() {
        return config.getString(APIMAN_MANAGER_SECURITY_CONTEXT_TYPE, "default"); //$NON-NLS-1$
    }

    /**
     * @return the configured storage type
     */
    public String getStorageType() {
        return config.getString(APIMAN_MANAGER_STORAGE_TYPE, "jpa"); //$NON-NLS-1$
    }

    /**
     * @return the configured storage query type
     */
    public String getStorageQueryType() {
        return config.getString(APIMAN_MANAGER_STORAGE_QUERY_TYPE, "jpa"); //$NON-NLS-1$
    }

    /**
     * @return the configured storage query type
     */
    public String getIdmStorageType() {
        return config.getString(APIMAN_MANAGER_IDM_STORAGE_TYPE, getStorageType());
    }

    /**
     * @return the elasticsearch protocol
     */
    public String getStorageESProtocol() {
        return config.getString(APIMAN_MANAGER_STORAGE_ES_PROTOCOL, "http"); //$NON-NLS-1$
    }

    /**
     * @return the elasticsearch host
     */
    public String getStorageESHost() {
        return config.getString(APIMAN_MANAGER_STORAGE_ES_HOST, "localhost"); //$NON-NLS-1$
    }

    /**
     * @return the elasticsearch port
     */
    public int getStorageESPort() {
        return config.getInt(APIMAN_MANAGER_STORAGE_ES_PORT, 19300);
    }

    /**
     * @return the elasticsearch cluster name
     */
    public String getStorageESClusterName() {
        return config.getString(APIMAN_MANAGER_STORAGE_ES_CLUSTER_NAME, DEFAULT_ES_CLUSTER_NAME);
    }

    /**
     * @return true if the elasticsearch index should be initialized if not found
     */
    public boolean isInitializeStorageES() {
        return config.getBoolean(APIMAN_MANAGER_STORAGE_ES_INITIALIZE, true);
    }

    /**
     * @return the configured storage type
     */
    public String getMetricsType() {
        return config.getString(APIMAN_MANAGER_METRICS_TYPE, "es"); //$NON-NLS-1$
    }

    /**
     * @return the elasticsearch protocol
     */
    public String getMetricsESProtocol() {
        return config.getString(APIMAN_MANAGER_METRICS_ES_PROTOCOL, "http"); //$NON-NLS-1$
    }

    /**
     * @return the elasticsearch host
     */
    public String getMetricsESHost() {
        return config.getString(APIMAN_MANAGER_METRICS_ES_HOST, "localhost"); //$NON-NLS-1$
    }

    /**
     * @return the elasticsearch port
     */
    public int getMetricsESPort() {
        return config.getInt(APIMAN_MANAGER_METRICS_ES_PORT, 19200);
    }

    /**
     * @return the elasticsearch cluster name
     */
    public String getMetricsESClusterName() {
        return config.getString(APIMAN_MANAGER_METRICS_ES_CLUSTER_NAME, DEFAULT_ES_CLUSTER_NAME);
    }

    /**
     * @return any custom properties associated with the storage (useful for custom impls)
     */
    public Map<String, String> getStorageProperties() {
        return getPrefixedProperties("apiman-manager.storage."); //$NON-NLS-1$
    }

    /**
     * @return any custom properties associated with the storage query impl
     */
    public Map<String, String> getStorageQueryProperties() {
        return getPrefixedProperties("apiman-manager.storage-query.type."); //$NON-NLS-1$
    }

    /**
     * @return any custom properties associated with the IDM storage impl (useful for custom impls)
     */
    public Map<String, String> getIdmStorageProperties() {
        return getPrefixedProperties("apiman-manager.idm-storage."); //$NON-NLS-1$
    }

    /**
     * @return any custom properties associated with the metrics accessor impl
     */
    public Map<String, String> getMetricsProperties() {
        return getPrefixedProperties("apiman-manager.metrics."); //$NON-NLS-1$
    }

    /**
     * Gets a map of properties prefixed by the given string.
     */
    protected Map<String, String> getPrefixedProperties(String prefix) {
        Map<String, String> rval = new HashMap<>();
        Iterator<String> keys = getConfig().getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith(prefix)) {
                String value = getConfig().getString(key);
                key = key.substring(prefix.length());
                rval.put(key, value);
            }
        }
        return rval;
    }

    /**
     * @return the configuration
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * 'Simple', 'JSON' or FQDN with {@link com.t1t.digipolis.apim.core.loggingIApimanLogger} implementation.
     *
     * @return Logger name or FQDN
     */
    public String getLoggerName() {
        return config.getString(APIMAN_MANAGER_CONFIG_LOGGER);
    }

}
