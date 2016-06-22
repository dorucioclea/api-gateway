package com.t1t.digipolis.apim.common.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;

/**
 * Factory for creating a configuration object from apiman.properties.
 *
 */
public class ConfigFactory {

    static {
        ConfigurationInterpolator.registerGlobalLookup("env", new EnvLookup()); //$NON-NLS-1$
        ConfigurationInterpolator.registerGlobalLookup("crypt", new CryptLookup()); //$NON-NLS-1$
        ConfigurationInterpolator.registerGlobalLookup("vault", new VaultLookup()); //$NON-NLS-1$
    }

    public static final CompositeConfiguration createConfig() {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.addConfiguration(new SystemPropertiesConfiguration());
        compositeConfiguration.addConfiguration(ConfigFileConfiguration.create("apiman.properties")); //$NON-NLS-1$
        return compositeConfiguration;
    }

}
