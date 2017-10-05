package com.t1t.apim;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class AppConfigBean {

    private String configPath;
    private String environment;
    private String kongHost;
    private String version;
    private String buildDate;
    private String dataDogMetricsApiKey;
    private String dataDogMetricsUri;
    private String dataDogMetricsApplicationKey;
    private Boolean restResourceSecurity;
    private Boolean restAuthResourceSecurity;
    private Boolean notificationsEnableDebug;
    private String notificationStartupMail;
    private String notificationMailFrom;
    private Integer hystrixMetricsTimeout;
    private String localFilePath;
    private String apiEngineName;
    private String apiEngineRequestPath;
    private Boolean apiEngineStripRequestPath;
    private String apiEngineUpstreamUrl;
    private String apiEngineAuthName;
    private Boolean apiEngineAuthStripRequestPath;
    private String apiEngineAuthUpstreamUrl;
    private String apiEngineAuthRequestPath;
    private String gatewaykeysName;
    private Boolean gatewaykeysStripRequestPath;
    private String gatewaykeysUpstreamUrl;
    private String gatewaykeysRequestPath;
    private String clusterInfoName;
    private String clusterInfoRequestPath;
    private String clusterInfoUpstreamUrl;
    private Boolean clusterInfoStripRequestPath;

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getKongHost() {
        return kongHost;
    }

    public void setKongHost(String kongHost) {
        this.kongHost = kongHost;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getDataDogMetricsApiKey() {
        return dataDogMetricsApiKey;
    }

    public void setDataDogMetricsApiKey(String dataDogMetricsApiKey) {
        this.dataDogMetricsApiKey = dataDogMetricsApiKey;
    }

    public String getDataDogMetricsUri() {
        return dataDogMetricsUri;
    }

    public void setDataDogMetricsUri(String dataDogMetricsUri) {
        this.dataDogMetricsUri = dataDogMetricsUri;
    }

    public String getDataDogMetricsApplicationKey() {
        return dataDogMetricsApplicationKey;
    }

    public void setDataDogMetricsApplicationKey(String dataDogMetricsApplicationKey) {
        this.dataDogMetricsApplicationKey = dataDogMetricsApplicationKey;
    }

    public Boolean getRestResourceSecurity() {
        return restResourceSecurity;
    }

    public void setRestResourceSecurity(Boolean restResourceSecurity) {
        this.restResourceSecurity = restResourceSecurity;
    }

    public Boolean getRestAuthResourceSecurity() {
        return restAuthResourceSecurity;
    }

    public void setRestAuthResourceSecurity(Boolean restAuthResourceSecurity) {
        this.restAuthResourceSecurity = restAuthResourceSecurity;
    }

    public Boolean getNotificationsEnableDebug() {
        return notificationsEnableDebug;
    }

    public void setNotificationsEnableDebug(Boolean notificationsEnableDebug) {
        this.notificationsEnableDebug = notificationsEnableDebug;
    }

    public String getNotificationStartupMail() {
        return notificationStartupMail;
    }

    public void setNotificationStartupMail(String notificationStartupMail) {
        this.notificationStartupMail = notificationStartupMail;
    }

    public String getNotificationMailFrom() {
        return notificationMailFrom;
    }

    public void setNotificationMailFrom(String notificationMailFrom) {
        this.notificationMailFrom = notificationMailFrom;
    }

    public Integer getHystrixMetricsTimeout() {
        return hystrixMetricsTimeout;
    }

    public void setHystrixMetricsTimeout(Integer hystrixMetricsTimeout) {
        this.hystrixMetricsTimeout = hystrixMetricsTimeout;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getApiEngineName() {
        return apiEngineName;
    }

    public void setApiEngineName(String apiEngineName) {
        this.apiEngineName = apiEngineName;
    }

    public String getApiEngineRequestPath() {
        return apiEngineRequestPath;
    }

    public void setApiEngineRequestPath(String apiEngineRequestPath) {
        this.apiEngineRequestPath = apiEngineRequestPath;
    }

    public Boolean getApiEngineStripRequestPath() {
        return apiEngineStripRequestPath;
    }

    public void setApiEngineStripRequestPath(Boolean apiEngineStripRequestPath) {
        this.apiEngineStripRequestPath = apiEngineStripRequestPath;
    }

    public String getApiEngineUpstreamUrl() {
        return apiEngineUpstreamUrl;
    }

    public void setApiEngineUpstreamUrl(String apiEngineUpstreamUrl) {
        this.apiEngineUpstreamUrl = apiEngineUpstreamUrl;
    }

    public String getApiEngineAuthName() {
        return apiEngineAuthName;
    }

    public void setApiEngineAuthName(String apiEngineAuthName) {
        this.apiEngineAuthName = apiEngineAuthName;
    }

    public Boolean getApiEngineAuthStripRequestPath() {
        return apiEngineAuthStripRequestPath;
    }

    public void setApiEngineAuthStripRequestPath(Boolean apiEngineAuthStripRequestPath) {
        this.apiEngineAuthStripRequestPath = apiEngineAuthStripRequestPath;
    }

    public String getApiEngineAuthUpstreamUrl() {
        return apiEngineAuthUpstreamUrl;
    }

    public void setApiEngineAuthUpstreamUrl(String apiEngineAuthUpstreamUrl) {
        this.apiEngineAuthUpstreamUrl = apiEngineAuthUpstreamUrl;
    }

    public String getApiEngineAuthRequestPath() {
        return apiEngineAuthRequestPath;
    }

    public void setApiEngineAuthRequestPath(String apiEngineAuthRequestPath) {
        this.apiEngineAuthRequestPath = apiEngineAuthRequestPath;
    }

    public String getGatewaykeysName() {
        return gatewaykeysName;
    }

    public void setGatewaykeysName(String gatewaykeysName) {
        this.gatewaykeysName = gatewaykeysName;
    }

    public Boolean getGatewaykeysStripRequestPath() {
        return gatewaykeysStripRequestPath;
    }

    public void setGatewaykeysStripRequestPath(Boolean gatewaykeysStripRequestPath) {
        this.gatewaykeysStripRequestPath = gatewaykeysStripRequestPath;
    }

    public String getGatewaykeysUpstreamUrl() {
        return gatewaykeysUpstreamUrl;
    }

    public void setGatewaykeysUpstreamUrl(String gatewaykeysUpstreamUrl) {
        this.gatewaykeysUpstreamUrl = gatewaykeysUpstreamUrl;
    }

    public String getGatewaykeysRequestPath() {
        return gatewaykeysRequestPath;
    }

    public void setGatewaykeysRequestPath(String gatewaykeysRequestPath) {
        this.gatewaykeysRequestPath = gatewaykeysRequestPath;
    }

    public String getClusterInfoName() {
        return clusterInfoName;
    }

    public void setClusterInfoName(String clusterInfoName) {
        this.clusterInfoName = clusterInfoName;
    }

    public String getClusterInfoRequestPath() {
        return clusterInfoRequestPath;
    }

    public void setClusterInfoRequestPath(String clusterInfoRequestPath) {
        this.clusterInfoRequestPath = clusterInfoRequestPath;
    }

    public String getClusterInfoUpstreamUrl() {
        return clusterInfoUpstreamUrl;
    }

    public void setClusterInfoUpstreamUrl(String clusterInfoUpstreamUrl) {
        this.clusterInfoUpstreamUrl = clusterInfoUpstreamUrl;
    }

    public Boolean getClusterInfoStripRequestPath() {
        return clusterInfoStripRequestPath;
    }

    public void setClusterInfoStripRequestPath(Boolean clusterInfoStripRequestPath) {
        this.clusterInfoStripRequestPath = clusterInfoStripRequestPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppConfigBean)) return false;

        AppConfigBean that = (AppConfigBean) o;

        if (configPath != null ? !configPath.equals(that.configPath) : that.configPath != null) return false;
        if (environment != null ? !environment.equals(that.environment) : that.environment != null) return false;
        if (kongHost != null ? !kongHost.equals(that.kongHost) : that.kongHost != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (buildDate != null ? !buildDate.equals(that.buildDate) : that.buildDate != null) return false;
        if (dataDogMetricsApiKey != null ? !dataDogMetricsApiKey.equals(that.dataDogMetricsApiKey) : that.dataDogMetricsApiKey != null)
            return false;
        if (dataDogMetricsUri != null ? !dataDogMetricsUri.equals(that.dataDogMetricsUri) : that.dataDogMetricsUri != null)
            return false;
        if (dataDogMetricsApplicationKey != null ? !dataDogMetricsApplicationKey.equals(that.dataDogMetricsApplicationKey) : that.dataDogMetricsApplicationKey != null)
            return false;
        if (restResourceSecurity != null ? !restResourceSecurity.equals(that.restResourceSecurity) : that.restResourceSecurity != null)
            return false;
        if (restAuthResourceSecurity != null ? !restAuthResourceSecurity.equals(that.restAuthResourceSecurity) : that.restAuthResourceSecurity != null)
            return false;
        if (notificationsEnableDebug != null ? !notificationsEnableDebug.equals(that.notificationsEnableDebug) : that.notificationsEnableDebug != null)
            return false;
        if (notificationStartupMail != null ? !notificationStartupMail.equals(that.notificationStartupMail) : that.notificationStartupMail != null)
            return false;
        if (notificationMailFrom != null ? !notificationMailFrom.equals(that.notificationMailFrom) : that.notificationMailFrom != null)
            return false;
        if (hystrixMetricsTimeout != null ? !hystrixMetricsTimeout.equals(that.hystrixMetricsTimeout) : that.hystrixMetricsTimeout != null)
            return false;
        if (localFilePath != null ? !localFilePath.equals(that.localFilePath) : that.localFilePath != null)
            return false;
        if (apiEngineName != null ? !apiEngineName.equals(that.apiEngineName) : that.apiEngineName != null)
            return false;
        if (apiEngineRequestPath != null ? !apiEngineRequestPath.equals(that.apiEngineRequestPath) : that.apiEngineRequestPath != null)
            return false;
        if (apiEngineStripRequestPath != null ? !apiEngineStripRequestPath.equals(that.apiEngineStripRequestPath) : that.apiEngineStripRequestPath != null)
            return false;
        if (apiEngineUpstreamUrl != null ? !apiEngineUpstreamUrl.equals(that.apiEngineUpstreamUrl) : that.apiEngineUpstreamUrl != null)
            return false;
        if (apiEngineAuthName != null ? !apiEngineAuthName.equals(that.apiEngineAuthName) : that.apiEngineAuthName != null)
            return false;
        if (apiEngineAuthStripRequestPath != null ? !apiEngineAuthStripRequestPath.equals(that.apiEngineAuthStripRequestPath) : that.apiEngineAuthStripRequestPath != null)
            return false;
        if (apiEngineAuthUpstreamUrl != null ? !apiEngineAuthUpstreamUrl.equals(that.apiEngineAuthUpstreamUrl) : that.apiEngineAuthUpstreamUrl != null)
            return false;
        if (apiEngineAuthRequestPath != null ? !apiEngineAuthRequestPath.equals(that.apiEngineAuthRequestPath) : that.apiEngineAuthRequestPath != null)
            return false;
        if (gatewaykeysName != null ? !gatewaykeysName.equals(that.gatewaykeysName) : that.gatewaykeysName != null)
            return false;
        if (gatewaykeysStripRequestPath != null ? !gatewaykeysStripRequestPath.equals(that.gatewaykeysStripRequestPath) : that.gatewaykeysStripRequestPath != null)
            return false;
        if (gatewaykeysUpstreamUrl != null ? !gatewaykeysUpstreamUrl.equals(that.gatewaykeysUpstreamUrl) : that.gatewaykeysUpstreamUrl != null)
            return false;
        if (gatewaykeysRequestPath != null ? !gatewaykeysRequestPath.equals(that.gatewaykeysRequestPath) : that.gatewaykeysRequestPath != null)
            return false;
        if (clusterInfoName != null ? !clusterInfoName.equals(that.clusterInfoName) : that.clusterInfoName != null)
            return false;
        if (clusterInfoRequestPath != null ? !clusterInfoRequestPath.equals(that.clusterInfoRequestPath) : that.clusterInfoRequestPath != null)
            return false;
        if (clusterInfoUpstreamUrl != null ? !clusterInfoUpstreamUrl.equals(that.clusterInfoUpstreamUrl) : that.clusterInfoUpstreamUrl != null)
            return false;
        if (clusterInfoStripRequestPath != null ? !clusterInfoStripRequestPath.equals(that.clusterInfoStripRequestPath) : that.clusterInfoStripRequestPath != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = configPath != null ? configPath.hashCode() : 0;
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (kongHost != null ? kongHost.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (buildDate != null ? buildDate.hashCode() : 0);
        result = 31 * result + (dataDogMetricsApiKey != null ? dataDogMetricsApiKey.hashCode() : 0);
        result = 31 * result + (dataDogMetricsUri != null ? dataDogMetricsUri.hashCode() : 0);
        result = 31 * result + (dataDogMetricsApplicationKey != null ? dataDogMetricsApplicationKey.hashCode() : 0);
        result = 31 * result + (restResourceSecurity != null ? restResourceSecurity.hashCode() : 0);
        result = 31 * result + (restAuthResourceSecurity != null ? restAuthResourceSecurity.hashCode() : 0);
        result = 31 * result + (notificationsEnableDebug != null ? notificationsEnableDebug.hashCode() : 0);
        result = 31 * result + (notificationStartupMail != null ? notificationStartupMail.hashCode() : 0);
        result = 31 * result + (notificationMailFrom != null ? notificationMailFrom.hashCode() : 0);
        result = 31 * result + (hystrixMetricsTimeout != null ? hystrixMetricsTimeout.hashCode() : 0);
        result = 31 * result + (localFilePath != null ? localFilePath.hashCode() : 0);
        result = 31 * result + (apiEngineName != null ? apiEngineName.hashCode() : 0);
        result = 31 * result + (apiEngineRequestPath != null ? apiEngineRequestPath.hashCode() : 0);
        result = 31 * result + (apiEngineStripRequestPath != null ? apiEngineStripRequestPath.hashCode() : 0);
        result = 31 * result + (apiEngineUpstreamUrl != null ? apiEngineUpstreamUrl.hashCode() : 0);
        result = 31 * result + (apiEngineAuthName != null ? apiEngineAuthName.hashCode() : 0);
        result = 31 * result + (apiEngineAuthStripRequestPath != null ? apiEngineAuthStripRequestPath.hashCode() : 0);
        result = 31 * result + (apiEngineAuthUpstreamUrl != null ? apiEngineAuthUpstreamUrl.hashCode() : 0);
        result = 31 * result + (apiEngineAuthRequestPath != null ? apiEngineAuthRequestPath.hashCode() : 0);
        result = 31 * result + (gatewaykeysName != null ? gatewaykeysName.hashCode() : 0);
        result = 31 * result + (gatewaykeysStripRequestPath != null ? gatewaykeysStripRequestPath.hashCode() : 0);
        result = 31 * result + (gatewaykeysUpstreamUrl != null ? gatewaykeysUpstreamUrl.hashCode() : 0);
        result = 31 * result + (gatewaykeysRequestPath != null ? gatewaykeysRequestPath.hashCode() : 0);
        result = 31 * result + (clusterInfoName != null ? clusterInfoName.hashCode() : 0);
        result = 31 * result + (clusterInfoRequestPath != null ? clusterInfoRequestPath.hashCode() : 0);
        result = 31 * result + (clusterInfoUpstreamUrl != null ? clusterInfoUpstreamUrl.hashCode() : 0);
        result = 31 * result + (clusterInfoStripRequestPath != null ? clusterInfoStripRequestPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppConfigBean{" +
                "configPath='" + configPath + '\'' +
                ", environment='" + environment + '\'' +
                ", kongHost='" + kongHost + '\'' +
                ", version='" + version + '\'' +
                ", buildDate='" + buildDate + '\'' +
                ", dataDogMetricsApiKey='" + dataDogMetricsApiKey + '\'' +
                ", dataDogMetricsUri='" + dataDogMetricsUri + '\'' +
                ", dataDogMetricsApplicationKey='" + dataDogMetricsApplicationKey + '\'' +
                ", restResourceSecurity=" + restResourceSecurity +
                ", restAuthResourceSecurity=" + restAuthResourceSecurity +
                ", notificationsEnableDebug=" + notificationsEnableDebug +
                ", notificationStartupMail='" + notificationStartupMail + '\'' +
                ", notificationMailFrom='" + notificationMailFrom + '\'' +
                ", hystrixMetricsTimeout=" + hystrixMetricsTimeout +
                ", localFilePath='" + localFilePath + '\'' +
                ", apiEngineName='" + apiEngineName + '\'' +
                ", apiEngineRequestPath='" + apiEngineRequestPath + '\'' +
                ", apiEngineStripRequestPath=" + apiEngineStripRequestPath +
                ", apiEngineUpstreamUrl='" + apiEngineUpstreamUrl + '\'' +
                ", apiEngineAuthName='" + apiEngineAuthName + '\'' +
                ", apiEngineAuthStripRequestPath=" + apiEngineAuthStripRequestPath +
                ", apiEngineAuthUpstreamUrl='" + apiEngineAuthUpstreamUrl + '\'' +
                ", apiEngineAuthRequestPath='" + apiEngineAuthRequestPath + '\'' +
                ", gatewaykeysName='" + gatewaykeysName + '\'' +
                ", gatewaykeysStripRequestPath=" + gatewaykeysStripRequestPath +
                ", gatewaykeysUpstreamUrl='" + gatewaykeysUpstreamUrl + '\'' +
                ", gatewaykeysRequestPath='" + gatewaykeysRequestPath + '\'' +
                ", clusterInfoName='" + clusterInfoName + '\'' +
                ", clusterInfoRequestPath='" + clusterInfoRequestPath + '\'' +
                ", clusterInfoUpstreamUrl='" + clusterInfoUpstreamUrl + '\'' +
                ", clusterInfoStripRequestPath=" + clusterInfoStripRequestPath +
                '}';
    }
}