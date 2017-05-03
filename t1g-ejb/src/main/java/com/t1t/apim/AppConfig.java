package com.t1t.apim;

import com.t1t.apim.beans.config.ConfigBean;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
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
@Singleton(name = "AppConfig")
@Startup
public class AppConfig implements Serializable {
    private static Config config;
    private static Properties properties;
    private static Logger _LOG = LoggerFactory.getLogger(AppConfig.class.getName());
    @Inject private IStorage storageService;

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
                throw ExceptionFactory.systemErrorException("Could not start the service, missing configuration.");
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
        }else throw ExceptionFactory.systemErrorException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        if(!configPath.toFile().exists()) throw ExceptionFactory.systemErrorException("API Engine config property file not found.");
        config = ConfigFactory.parseFile(configPath.toFile());
        //config = ConfigFactory.load(getConfigurationFile());
        if(config==null) throw ExceptionFactory.systemErrorException("API Engine log not found");
        else{
            try {
                _LOG.info("===== API Engine configuration ==============================");
                _LOG.info("Using configuration file: {}", getConfigurationFile());
                _LOG.info("Build: {}", getBuildDate());
                _LOG.info("version: {}", getVersion());
                _LOG.info("environment: {}", getEnvironment());
                _LOG.info("Kong host: {}", getKongHost());
                _LOG.info("Kong endpoint: {}", getKongEndpoint());
                _LOG.info("Kong management endpoint: {}", getKongManagementEndpoint());
                _LOG.info("REST resource security: {}", getRestResourceSecurity());
                _LOG.info("REST AUTH resource security: {}", getRestAuthResourceSecurity());
                _LOG.info("Notifications: debug enabled? {}", getNotificationsEnableDebug());
                _LOG.info("Notifications: startup mail will be sent to {}", getNotificationStartupMail());
                _LOG.info("Notifications: mail will be send from {}", getNotificationMailFrom());
                _LOG.info("Metrics engine timeout value: {}", getHystrixMetricsTimeout());
                _LOG.info("Local file path: {}", getLocalFilePath());
                _LOG.info("API Engine web upstream url: {}", getApiEngineUpstream());
                _LOG.info("API Engine web request path: {}", getApiEngineRequestPath());
                _LOG.info("API Engine auth upstream url: {}", getApiEngineAuthUpstreamUrl());
                _LOG.info("API Engine auth request path: {}", getApiEngineAuthRequestPath());
                _LOG.info("Gateway keys upstream url: {}", getGatewaykeysUpstreamUrl());
                _LOG.info("Gateway keys request path: {}", getGatewaykeysRequestPath());
                _LOG.info("Gateway cluster info url: {}", getGatewaykeysUpstreamUrl());
                _LOG.info("Gateway cluster info request path: {}", getGatewaykeysRequestPath());
                _LOG.info("=============================================================");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getEnvironment(){return config.getString(IConfig.APP_ENVIRONMENT);}
    public String getKongHost(){return config.getString(IConfig.KONG_HOST);}
    public String getKongEndpoint(){return config.getString(IConfig.KONG_URL);}
    public String getVersion(){return properties.getProperty(IConfig.PROP_FILE_VERSION);}
    public String getBuildDate(){return properties.getProperty(IConfig.PROP_FILE_DATE);}
    public String getConfigurationFile(){return properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE);}
    public String getKongManagementEndpoint(){return config.getString(IConfig.KONG_URL_MANAGEMENT);}
    public String getDataDogMetricsApiKey(){return config.getString(IConfig.DATADOG_METRICS_API_KEY);}
    public String getDataDogMetricsURI(){return config.getString(IConfig.DATADOG_METRICS_URI);}
    public String getDataDogMetricsApplicationKey(){return config.getString(IConfig.DATADOG_METRICS_APPLICATION_KEY);}
    public Boolean getRestResourceSecurity(){return config.getBoolean(IConfig.SECURITY_REST_RESORUCES);}
    public Boolean getRestAuthResourceSecurity(){return config.getBoolean(IConfig.SECURITY_REST_AUTH_RESOURCES);}
    public Boolean getNotificationsEnableDebug(){return config.getBoolean(IConfig.NOTIFICATION_ENABLE_DEBUG);}
    public String getNotificationStartupMail(){return config.getString(IConfig.NOTIFICATION_STARTUP_MAIL);}
    public String getNotificationMailFrom(){return config.getString(IConfig.NOTIFICATION_MAIL_FROM);}
    public Integer getHystrixMetricsTimeout() {return config.getInt(IConfig.HYSTRIX_METRICS_TIMEOUT_VALUE);}
    public String getLocalFilePath() {return config.getString(IConfig.FILEPATH_LOCAL);}
    public String getApiEngineName(){return config.getString(IConfig.GWD_APIENGINE_NAME);}
    public String getApiEngineRequestPath(){return config.getString(IConfig.GWD_APIENGINE_REQUEST_PATH);}
    public Boolean getApiEngineStripRequestPath(){return config.getBoolean(IConfig.GWD_APIENGINE_STRIP_REQUEST_PATH);}
    public String getApiEngineUpstream(){return config.getString(IConfig.GWD_APIENGINE_UPSTREAM_URL);}
    public String getApiEngineAuthName(){return config.getString(IConfig.GWD_APIENGINEAUTH_NAME);}
    public Boolean getApiEngineAuthStripRequestPath(){return config.getBoolean(IConfig.GWD_APIENGINEAUTH_STRIP_REQUEST_PATH);}
    public String getApiEngineAuthUpstreamUrl(){return config.getString(IConfig.GWD_APIENGINEAUTH_UPSTREAM_URL);}
    public String getApiEngineAuthRequestPath(){return config.getString(IConfig.GWD_APIENGINEAUTH_REQUEST_PATH);}
    public String getGatewaykeysName(){return config.getString(IConfig.GWD_GATEWAYKEYS_NAME);}
    public Boolean getGatewaykeysStripRequestPath(){return config.getBoolean(IConfig.GWD_GATEWAYKEYS_STRIP_REQUEST_PATH);}
    public String getGatewaykeysUpstreamUrl(){return config.getString(IConfig.GWD_GATEWAYKEYS_UPSTREAM_URL);}
    public String getGatewaykeysRequestPath(){return config.getString(IConfig.GWD_GATEWAYKEYS_REQUEST_PATH);}
    public String getClusterInfoName() {return config.getString(IConfig.GWD_CLUSTER_INFO_NAME);}
    public String getClusterInfoRequestPath() {return config.getString(IConfig.GWD_CLUSTER_INFO_REQUEST_PATH);}
    public String getClusterInfoUpstreamUrl() {return config.getString(IConfig.GWD_CLUSTER_INFO_UPSTREAM_URL);}
    public Boolean getClusterInfoStripRequestPath() {return config.getBoolean(IConfig.GWD_CLUSTER_INFO_STRIP_REQUEST_PATH);}

}
