package com.t1t.digipolis.apim;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * Created by michallispashidis on 10/10/15.
 * This will load the configuration for a specific environment and produce the config throughout the application.
 * More information about the config Typesafe approach can be found: https://github.com/typesafehub/config#essential-information
 */
@Singleton
@ApplicationScoped
@Startup
public class AppConfig implements Serializable {
    private static Config config;
    private static Properties properties;
    private static Logger _LOG = LoggerFactory.getLogger(AppConfig.class.getName());

    public AppConfig() {
        init();
    }

    public void init(){
        //read properties file
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        if(is!=null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else throw new RuntimeException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        config = ConfigFactory.load(getConfigurationFile()); if(config==null) throw new RuntimeException("API Engine log not found");else{
            _LOG.info("===== API Engine configruation ==============================");
            _LOG.info("Using configuration file: {}",getConfigurationFile());
            _LOG.info("Build: {}",getBuildDate());
            _LOG.info("version: {}",getVersion());
            _LOG.info("environment: {}",getEnvironment());
            _LOG.info("Kong endpoint: {}",getKongEndpoint());
            _LOG.info("Kong management endpoint: {}",getKongManagementEndpoint());
            _LOG.info("IDP SAML2 endpoint: {}",getIDPSAMLEndpoint());
            _LOG.info("IDP NameID format: {}",getIDPSAMLNameIdFormat());
            _LOG.info("IDP SCIM endpoint for users: {}",getIDPSCIMEndpointUsers());
            _LOG.info("IDP SCIM endpoint for groups: {}",getIDPSCIMEndpointGroups());
            _LOG.info("IDP OAUTH token endpoint: {}",getIDPOAuthTokenEndpoint());
            _LOG.info("IDP OAUTH client-id: {}",getIDPOAuthClientId());
            _LOG.info("IDP OAUTH client-secret: {}",getIDPOAuthClientSecret());
            _LOG.info("Metrics schema: {}",getMetricsScheme());
            _LOG.info("Metrics URI: {}",getMetricsURI());
            _LOG.info("Metrics port: {}",getMetricsPort());
            _LOG.info("Default user organization: {}",getDefaultOrganization());
            _LOG.info("Default user roles: {}",getDefaultUserRoles());
            _LOG.info("Consent page: {}",getOAuthConsentURI());
            _LOG.info("=============================================================");
        };
    }

    public String getEnvironment(){return config.getString(IConfig.APP_ENVIRONMENT);}
    public String getKongEndpoint(){return config.getString(IConfig.KONG_URL);}
    public String getVersion(){return properties.getProperty(IConfig.PROP_FILE_VERSION);}
    public String getBuildDate(){return properties.getProperty(IConfig.PROP_FILE_DATE);}
    public String getConfigurationFile(){return properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE);}
    public String getKongManagementEndpoint(){return config.getString(IConfig.KONG_URL_MANAGEMENT);}
    public String getIDPSAMLEndpoint(){return config.getString(IConfig.IDP_SAML_ENDPOINT);}
    public String getIDPSAMLNameIdFormat(){return config.getString(IConfig.IDP_NAMEID_FORMAT);}
    public String getIDPSCIMEndpointUsers(){return config.getString(IConfig.IDP_SCIM_ENDPOINT_USERS);}
    public String getIDPSCIMEndpointGroups(){return config.getString(IConfig.IDP_SCIM_ENDPOINT_GROUPS);}
    public String getIDPOAuthTokenEndpoint(){return config.getString(IConfig.IDP_OAUTH_TOKEN_ENDPOINT);}
    public String getIDPOAuthClientId(){return config.getString(IConfig.IDP_OAUTH_CLIENT_ID);}
    public String getIDPOAuthClientSecret(){return config.getString(IConfig.IDP_OAUTH_CLIENT_SECRET);}
    public String getMetricsScheme(){return config.getString(IConfig.METRICS_SCHEME);}
    public String getMetricsURI(){return config.getString(IConfig.METRICS_DNS);}
    public String getMetricsPort(){return config.getString(IConfig.METRICS_PORT);}
    public String getDefaultOrganization(){return config.getString(IConfig.DEFAULT_USER_ORGANIZATION);}
    public String getDefaultUserRoles(){return config.getString(IConfig.DEFAULT_USER_ROLES_FOR_DEFAULT_ORG);}
    public String getOAuthConsentURI(){return config.getString(IConfig.CONSENT_URI);}
}
