package com.t1t.apim.common.config;

import com.t1t.apim.exceptions.ExceptionFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A configuration that comes from a properties configuration file.  This
 * implementation will load the properties config file from wherever it can
 * reasonably find it.
 */
public class ConfigFileConfiguration extends PropertiesConfiguration {

    /**
     * Constructor.
     *
     * @param configFileName
     * @throws ConfigurationException
     */
    private ConfigFileConfiguration(String configFileName) throws ConfigurationException {
        super(discoverConfigFileUrl(configFileName));
    }

    /**
     * Returns a URL to a file with the given name inside the given directory.
     *
     * @param directory
     */
    protected static URL findConfigUrlInDirectory(File directory, String configName) {
        if (directory.isDirectory()) {
            File cfile = new File(directory, configName);
            if (cfile.isFile()) {
                try {
                    return cfile.toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return null;
    }

    public static ConfigFileConfiguration create(String configFileName) {
        try {
            return new ConfigFileConfiguration(configFileName);
        } catch (ConfigurationException e) {
            throw ExceptionFactory.actionException("Failed to find configuration file: " + configFileName, e); //$NON-NLS-1$
        }
    }

    private static URL discoverConfigFileUrl(String configFileName) {
        // Wildfly/EAP takes priority
        ///////////////////////////////////
        String jbossConfigDir = System.getProperty("jboss.server.config.dir"); //$NON-NLS-1$
        String jbossConfigUrl = System.getProperty("jboss.server.config.url"); //$NON-NLS-1$
        URL rval = null;
        if (jbossConfigDir != null) {
            File dirFile = new File(jbossConfigDir);
            rval = findConfigUrlInDirectory(dirFile, configFileName);
            if (rval != null) {
                return rval;
            }
        }
        if (jbossConfigUrl != null) {
            File dirFile = new File(jbossConfigUrl);
            rval = findConfigUrlInDirectory(dirFile, configFileName);
            if (rval != null) {
                return rval;
            }
        }

        // If not found, use an empty file.
        ////////////////////////////////////////
        return ConfigFileConfiguration.class.getResource("empty.properties"); //$NON-NLS-1$
    }

}
