package com.t1t.digipolis.apim.cdi;

import com.t1t.digipolis.apim.core.plugin.AbstractPluginRegistry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * A wildfly 8 version of the plugin registry.  This subclass exists in order
 * to properly configure the data directory that should be used.  In this case
 * the data directory is $WILDFLY/standalone/data/apiman/plugins
 *
 * @author eric.wittmann@redhat.com
 */
@ApplicationScoped
public class Wildfly8PluginRegistry extends AbstractPluginRegistry {

    @Inject
    private WarApiManagerConfig config;
    
    private Set<URL> mavenRepos = null;
    
    /**
     * Creates the directory to use for the plugin registry.  The location of
     * the plugin registry is in the Wildfly data directory.
     */
    private static File getPluginDir() {
        String dataDirPath = System.getProperty("jboss.server.data.dir"); //$NON-NLS-1$
        File dataDir = new File(dataDirPath);
        if (!dataDir.isDirectory()) {
            throw new RuntimeException("Failed to find WildFly data directory at: " + dataDirPath); //$NON-NLS-1$
        }
        File pluginsDir = new File(dataDir, "apiman/plugins"); //$NON-NLS-1$
        return pluginsDir;
    }

    /**
     * Constructor.
     */
    public Wildfly8PluginRegistry() {
        super(getPluginDir());
    }

    /**
     * @see AbstractPluginRegistry#getMavenRepositories()
     */
    @Override
    protected Set<URL> getMavenRepositories() {
        if (mavenRepos == null) {
            mavenRepos = loadMavenRepositories();
        }
        return mavenRepos;
    }

    /**
     * @return the maven repositories to use when downloading plugins
     */
    protected Set<URL> loadMavenRepositories() {
        Set<URL> repos = new HashSet<>();
        repos.addAll(super.getMavenRepositories());
        repos.addAll(config.getPluginRepositories());
        return repos;
    }
    
}
