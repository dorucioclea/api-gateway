package com.t1t.digipolis.apim;

import com.t1t.digipolis.apim.beans.config.ConfigBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * Created by michallispashidis on 10/10/15.
 * This will load the configuration for a specific environment and produce the config throughout the application.
 * More information about the config Typesafe approach can be found: https://github.com/typesafehub/config#essential-information
 */
@ApplicationScoped
public class AppConfig implements Serializable {
    private static Config config;
    private static Properties properties;
    private static Logger _LOG = LoggerFactory.getLogger(AppConfig.class.getName());
    @Inject private IStorage storageService;
    @Inject private StartupService startupService;

    @PostConstruct
    public void postInit() {
            initConfig(null);
    }

    public void initConfig(ConfigBean optionalConfig){
        final ConfigBean defaultConfig;
        if(optionalConfig==null){
            try {
                final List<ConfigBean> configList = storageService.getDefaultConfig();
                if(configList!=null && configList.size()>0) defaultConfig = configList.get(0);
                else throw new StorageException("No configuration found.");
            } catch (StorageException e) {
                throw new RuntimeException("Could not start the service, missing configuration.");
            }
        }else defaultConfig = optionalConfig;
        Path configPath = Paths.get(defaultConfig.getConfigPath());
        _LOG.info("Config path loaded:{}",configPath.toAbsolutePath());
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
        if(!configPath.toFile().exists()) throw new RuntimeException("API Engine config property file not found.");
        config = ConfigFactory.parseFile(configPath.toFile());
        //config = ConfigFactory.load(getConfigurationFile());
        if(config==null) throw new RuntimeException("API Engine log not found");
        else{
            _LOG.info("===== API Engine configuration ==============================");
            _LOG.info("Using configuration file: {}",getConfigurationFile());
            _LOG.info("Build: {}",getBuildDate());
            _LOG.info("version: {}",getVersion());
            _LOG.info("environment: {}",getEnvironment());
            _LOG.info("Kong host: {}",getKongHost());
            _LOG.info("Kong endpoint: {}",getKongEndpoint());
            _LOG.info("Kong management endpoint: {}",getKongManagementEndpoint());
            _LOG.info("IDP Entity ID: {}", getIDPEntityId());
            _LOG.info("IDP Audience URI: {}", getIDPAudienceURI());
            _LOG.info("IDP SAML2 endpoint: {}",getIDPSAMLEndpoint());
            _LOG.info("IDP NameID format: {}",getIDPSAMLNameIdFormat());
            _LOG.info("IDP OAUTH token endpoint: {}",getIDPOAuthTokenEndpoint());
            _LOG.info("IDP OAUTH client-id: {}",getIDPOAuthClientId());
            _LOG.info("IDP OAUTH client-secret: {}",getIDPOAuthClientSecret());
            _LOG.info("REST resource security: {}", getRestResourceSecurity());
            _LOG.info("REST AUTH resource security: {}", getRestAuthResourceSecurity());
            _LOG.info("Metrics schema: {}",getMetricsScheme());
            _LOG.info("Metrics URI: {}",getMetricsURI());
            _LOG.info("Metrics port: {}",getMetricsPort());
            _LOG.info("Default user organization: {}",getDefaultOrganization());
            _LOG.info("Default user roles: {}",getDefaultUserRoles());
            _LOG.info("Consent page: {}",getOAuthConsentURI());
            _LOG.info("Enable centralized OAuth2 token/authorization endpoints: {}",getOAuthEnableGatewayEnpoints());
            _LOG.info("JWT default token expiration (in minutes):{}", getJWTDefaultTokenExpInSeconds());
            _LOG.info("Analytics enables: {}",getAnalyticsHost());
            _LOG.info("Analytics send towards {} with port {} and service token {}",getAnalyticsHost(),getAnalyticsPort(),getAnalyticsServiceToken());
            _LOG.info("Notifications: debug enabled? {}", getNotificationsEnableDebug());
            _LOG.info("Notifications: startup mail will be sent to {}", getNotificationStartupMail());
            _LOG.info("Notifications: mail will be send from {}", getNotificationMailFrom());
            _LOG.info("Metrics engine timeout value: {}", getHystrixMetricsTimeout());
            _LOG.info("Local file path: {}", getLocalFilePath());
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
    public Boolean getOAuthEnableGatewayEnpoints(){return config.getBoolean(IConfig.OAUTH_ENABLE_GTW_ENDPOINTS);}
    public String getIDPSCIMUserLogin(){return config.getString(IConfig.IDP_SCIM_USER_LOGIN);}
    public String getIDPSCIMUserPassword(){return config.getString(IConfig.IDP_SCIM_USER_PWD);}
    public Integer getJWTDefaultTokenExpInSeconds(){return config.getInt(IConfig.JWT_DEFAULT_TOKEN_EXP);}
    public Boolean getRestResourceSecurity(){return config.getBoolean(IConfig.SECURITY_REST_RESORUCES);}
    public Boolean getRestAuthResourceSecurity(){return config.getBoolean(IConfig.SECURITY_REST_AUTH_RESOURCES);}
    public String getAnalyticsServiceToken(){return config.getString(IConfig.ANALYTICS_TOKEN);}
    public String getAnalyticsEnvironment(){return config.getString(IConfig.ANALYTICS_ENVIRONMENT);}
    public Integer getAnalyticsRetryCount(){return config.getInt(IConfig.ANALYTICS_RETRY_COUNT);}
    public Integer getAnalyticsQueueSize(){return config.getInt(IConfig.ANALYTICS_QUEUE_SIZE);}
    public Integer getAnalyticsFlushTimeout(){return config.getInt(IConfig.ANALYTICS_FLUSH_TIMEOUT);}
    public Boolean getAnalyticsLogBodies(){return config.getBoolean(IConfig.ANALYTICS_LOG_BODIES);}
    public Integer getAnalyticsConnTimeout(){return config.getInt(IConfig.ANALYTICS_CONN_TIMEOUT);}
    public String getAnalyticsHost(){return config.getString(IConfig.ANALYTICS_HOST);}
    public Integer getAnalyticsPort(){return config.getInt(IConfig.ANALYTICS_PORT);}
    public Boolean getAnalyticsHttps(){return config.getBoolean(IConfig.ANALYTICS_HTTPS);}
    public Boolean getAnalyticsHttpsVerify(){return config.getBoolean(IConfig.ANALYTICS_HTTPS_VERIFY);}
    public Boolean getNotificationsEnableDebug(){return config.getBoolean(IConfig.NOTIFICATION_ENABLE_DEBUG);}
    public String getNotificationStartupMail(){return config.getString(IConfig.NOTIFICATION_STARTUP_MAIL);}
    public String getNotificationMailFrom(){return config.getString(IConfig.NOTIFICATION_MAIL_FROM);}
    public Integer getHystrixMetricsTimeout() {return config.getInt(IConfig.HYSTRIX_METRICS_TIMEOUT_VALUE);}
    public String getLocalFilePath() {return config.getString(IConfig.FILEPATH_LOCAL);}
    public String getIDPEntityId(){return config.getString(IConfig.IDP_ENTITY_ID);}
    public String getIDPAudienceURI(){return config.getString(IConfig.IDP_AUDIENCE_URI);}
}
