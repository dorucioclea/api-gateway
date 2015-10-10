package com.t1t.digipolis.apim;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.PostActivate;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;

/**
 * Created by michallispashidis on 10/10/15.
 * This will load the configuration for a specific environment and produce the config throughout the application.
 * More information about the config Typesafe approach can be found: https://github.com/typesafehub/config#essential-information
 */
@Singleton
@Startup
public class AppConfig implements Serializable {
    private static String environment = "application"; //will load application.conf
    private static Config config;
    private static Logger _LOG = LoggerFactory.getLogger(AppConfig.class.getName());

    public AppConfig() {
        init();
    }

    public void init(){
        config = ConfigFactory.load(environment); if(config==null) throw new RuntimeException("API Engine log not found");else{
            _LOG.info("===== API Engine configruation ==============================");
            _LOG.info("version: {}",getVersion());
            _LOG.info("environment: {}",getEnvironment());
            _LOG.info("Kong endpoint: {}",getKongEndpoint());
            _LOG.info("Kong management endpoint: {}",getKongManagementEndpoint());
            _LOG.info("IDP SAML2 endpoint: {}",getIDPSAMLEndpoint());
            _LOG.info("IDP SCIM endpoint: {}",getIDPSCIMEndpoint());
            _LOG.info("Metrics schema: {}",getMetricsScheme());
            _LOG.info("Metrics URI: {}",getMetricsURI());
            _LOG.info("Metrics port: {}",getMetricsPort());
            _LOG.info("Default user organization: {}",getDefaultOrganization());
            _LOG.info("Default user roles: {}",getDefaultUserRoles());
            _LOG.info("Consent page: {}",getOAuthConsentURI());
            _LOG.info("=============================================================");
        };
    }

    public String getVersion(){return config.getString(IConfig.APP_VERSION);}
    public String getEnvironment(){return config.getString(IConfig.APP_ENVIRONMENT);}
    public String getKongEndpoint(){return config.getString(IConfig.KONG_URL);}
    public String getKongManagementEndpoint(){return config.getString(IConfig.KONG_URL_MANAGEMENT);}
    public String getIDPSAMLEndpoint(){return config.getString(IConfig.IDP_SAML_ENDPOINT);}
    public String getIDPSCIMEndpoint(){return config.getString(IConfig.IDP_SCIM_ENDPOINT);}
    public String getMetricsScheme(){return config.getString(IConfig.METRICS_SCHEME);}
    public String getMetricsURI(){return config.getString(IConfig.METRICS_DNS);}
    public String getMetricsPort(){return config.getString(IConfig.METRICS_PORT);}
    public String getDefaultOrganization(){return config.getString(IConfig.DEFAULT_USER_ORGANIZATION);}
    public String getDefaultUserRoles(){return config.getString(IConfig.DEFAULT_USER_ROLES_FOR_DEFAULT_ORG);}
    public String getOAuthConsentURI(){return config.getString(IConfig.CONSENT_URI);}

}
