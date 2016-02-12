package com.t1t.digipolis.apim;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
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
            _LOG.info("Kong host: {}",getKongHost());
            _LOG.info("Kong endpoint: {}",getKongEndpoint());
            _LOG.info("Kong management endpoint: {}",getKongManagementEndpoint());
            _LOG.info("IDP SAML2 endpoint: {}",getIDPSAMLEndpoint());
            _LOG.info("IDP NameID format: {}",getIDPSAMLNameIdFormat());
            _LOG.info("IDP SCIM endpoint: {}",getIDPSCIMEndpoint());
            _LOG.info("IDP SCIM user login has been configured?: {}",!StringUtils.isEmpty(getIDPSCIMUserLogin()));
            _LOG.info("IDP SCIM user password has been configured?: {}",!StringUtils.isEmpty(getIDPSCIMUserPassword()));
            _LOG.info("IDP OAUTH token endpoint: {}",getIDPOAuthTokenEndpoint());
            _LOG.info("IDP OAUTH client-id: {}",getIDPOAuthClientId());
            _LOG.info("IDP OAUTH client-secret: {}",getIDPOAuthClientSecret());
            _LOG.info("IDP SCIM activation: {}",getIDPSCIMActivation());
            _LOG.info("Metrics schema: {}",getMetricsScheme());
            _LOG.info("Metrics URI: {}",getMetricsURI());
            _LOG.info("Metrics port: {}",getMetricsPort());
            _LOG.info("Default user organization: {}",getDefaultOrganization());
            _LOG.info("Default user roles: {}",getDefaultUserRoles());
            _LOG.info("Consent page: {}",getOAuthConsentURI());
            _LOG.info("JWT default token expiration (in minutes):{}",getJWTDefaultTokenExpInMinutes());
            _LOG.info("Analytics enables: {}",getAnalyticsEnabled());
            _LOG.info("Analytics send towards {} with port {} and service token {}",getAnalyticsHost(),getAnalyticsPort(),getAnalyticsServiceToken());
            _LOG.info("=============================================================");
        };
    }

    public String getEnvironment(){return config.getString(IConfig.APP_ENVIRONMENT);}
    public String getKongHost(){return config.getString(IConfig.KONG_HOST);}
    public String getKongEndpoint(){return config.getString(IConfig.KONG_URL);}
    public String getVersion(){return properties.getProperty(IConfig.PROP_FILE_VERSION);}
    public String getBuildDate(){return properties.getProperty(IConfig.PROP_FILE_DATE);}
    public String getConfigurationFile(){return properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE);}
    public String getKongManagementEndpoint(){return config.getString(IConfig.KONG_URL_MANAGEMENT);}
    public String getIDPSAMLEndpoint(){return config.getString(IConfig.IDP_SAML_ENDPOINT);}
    public String getIDPSAMLNameIdFormat(){return config.getString(IConfig.IDP_NAMEID_FORMAT);}
    public String getIDPSCIMEndpoint(){return config.getString(IConfig.IDP_SCIM_ENDPOINT);}
    public String getIDPOAuthTokenEndpoint(){return config.getString(IConfig.IDP_OAUTH_TOKEN_ENDPOINT);}
    public String getIDPOAuthClientId(){return config.getString(IConfig.IDP_OAUTH_CLIENT_ID);}
    public String getIDPOAuthClientSecret(){return config.getString(IConfig.IDP_OAUTH_CLIENT_SECRET);}
    public Boolean getIDPSCIMActivation(){return config.getBoolean(IConfig.IDP_SCIM_ACTIVATE);}
    public String getMetricsScheme(){return config.getString(IConfig.METRICS_SCHEME);}
    public String getMetricsURI(){return config.getString(IConfig.METRICS_DNS);}
    public String getMetricsPort(){return config.getString(IConfig.METRICS_PORT);}
    public String getDefaultOrganization(){return config.getString(IConfig.DEFAULT_USER_ORGANIZATION);}
    public String getDefaultUserRoles(){return config.getString(IConfig.DEFAULT_USER_ROLES_FOR_DEFAULT_ORG);}
    public String getOAuthConsentURI(){return config.getString(IConfig.CONSENT_URI);}
    public String getIDPSCIMUserLogin(){return config.getString(IConfig.IDP_SCIM_USER_LOGIN);}
    public String getIDPSCIMUserPassword(){return config.getString(IConfig.IDP_SCIM_USER_PWD);}
    public Integer getJWTDefaultTokenExpInMinutes(){return config.getInt(IConfig.JWT_DEFAULT_TOKEN_EXP);}
    public Boolean getAnalyticsEnabled(){return config.getBoolean(IConfig.ANALYTICS_ENABLED);}
    public String getAnalyticsServiceToken(){return config.getString(IConfig.ANALYTICS_TOKEN);}
    public Integer getAnalyticsBatchSize(){return config.getInt(IConfig.ANALYTICS_BATCH_SIZE);}
    public Boolean getAnalyticsLogBody(){return config.getBoolean(IConfig.ANALYTICS_LOG_BODY);}
    public Integer getAnalyticsDelay(){return config.getInt(IConfig.ANALYTICS_DELAY);}
    public String getAnalyticsEnvironment(){return config.getString(IConfig.ANALYTICS_ENVIRONMENT);}
    public Integer getAnalyticsMaxSendingQueue(){return config.getInt(IConfig.ANALYTICS_MAX_SENDING_QUEUE);}
    public String getAnalyticsHost(){return config.getString(IConfig.ANALYTICS_HOST);}
    public Integer getAnalyticsPort(){return config.getInt(IConfig.ANALYTICS_PORT);}
}
