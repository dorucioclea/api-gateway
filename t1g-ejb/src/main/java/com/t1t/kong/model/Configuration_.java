
package com.t1t.kong.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Configuration_ {

    @SerializedName("nginx_acc_logs")
    @Expose
    private String nginxAccLogs;
    @SerializedName("cassandra_lb_policy")
    @Expose
    private String cassandraLbPolicy;
    @SerializedName("cassandra_port")
    @Expose
    private Long cassandraPort;
    @SerializedName("serf_path")
    @Expose
    private String serfPath;
    @SerializedName("nginx_kong_conf")
    @Expose
    private String nginxKongConf;
    @SerializedName("proxy_listen")
    @Expose
    private String proxyListen;
    @SerializedName("ssl_cert")
    @Expose
    private String sslCert;
    @SerializedName("lua_ssl_verify_depth")
    @Expose
    private Long luaSslVerifyDepth;
    @SerializedName("admin_listen")
    @Expose
    private String adminListen;
    @SerializedName("admin_listen_ssl")
    @Expose
    private String adminListenSsl;
    @SerializedName("ssl_cert_default")
    @Expose
    private String sslCertDefault;
    @SerializedName("cassandra_repl_factor")
    @Expose
    private Long cassandraReplFactor;
    @SerializedName("cluster_profile")
    @Expose
    private String clusterProfile;
    @SerializedName("upstream_keepalive")
    @Expose
    private Long upstreamKeepalive;
    @SerializedName("lua_package_cpath")
    @Expose
    private String luaPackageCpath;
    @SerializedName("cassandra_consistency")
    @Expose
    private String cassandraConsistency;
    @SerializedName("admin_ssl_cert_default")
    @Expose
    private String adminSslCertDefault;
    @SerializedName("pg_host")
    @Expose
    private String pgHost;
    @SerializedName("pg_port")
    @Expose
    private Long pgPort;
    @SerializedName("cluster_ttl_on_failure")
    @Expose
    private Long clusterTtlOnFailure;
    @SerializedName("nginx_err_logs")
    @Expose
    private String nginxErrLogs;
    @SerializedName("plugins")
    @Expose
    private Plugins_ plugins;
    @SerializedName("nginx_optimizations")
    @Expose
    private Boolean nginxOptimizations;
    @SerializedName("cassandra_keyspace")
    @Expose
    private String cassandraKeyspace;
    @SerializedName("cassandra_data_centers")
    @Expose
    private List<String> cassandraDataCenters = new ArrayList<String>();
    @SerializedName("ssl_cert_key")
    @Expose
    private String sslCertKey;
    @SerializedName("ssl")
    @Expose
    private Boolean ssl;
    @SerializedName("cassandra_ssl")
    @Expose
    private Boolean cassandraSsl;
    @SerializedName("admin_ssl")
    @Expose
    private Boolean adminSsl;
    @SerializedName("ssl_cert_key_default")
    @Expose
    private String sslCertKeyDefault;
    @SerializedName("anonymous_reports")
    @Expose
    private Boolean anonymousReports;
    @SerializedName("kong_env")
    @Expose
    private String kongEnv;
    @SerializedName("serf_log")
    @Expose
    private String serfLog;
    @SerializedName("log_level")
    @Expose
    private String logLevel;
    @SerializedName("proxy_listen_ssl")
    @Expose
    private String proxyListenSsl;
    @SerializedName("nginx_worker_processes")
    @Expose
    private String nginxWorkerProcesses;
    @SerializedName("cassandra_contact_points")
    @Expose
    private List<String> cassandraContactPoints = new ArrayList<String>();
    @SerializedName("proxy_ip")
    @Expose
    private String proxyIp;
    @SerializedName("pg_ssl")
    @Expose
    private Boolean pgSsl;
    @SerializedName("proxy_ssl_port")
    @Expose
    private Double proxySslPort;
    @SerializedName("pg_database")
    @Expose
    private String pgDatabase;
    @SerializedName("admin_ip")
    @Expose
    private String adminIp;
    @SerializedName("database")
    @Expose
    private String database;
    @SerializedName("proxy_port")
    @Expose
    private Double proxyPort;
    @SerializedName("admin_ssl_port")
    @Expose
    private Double adminSslPort;
    @SerializedName("nginx_pid")
    @Expose
    private String nginxPid;
    @SerializedName("proxy_ssl_ip")
    @Expose
    private String proxySslIp;
    @SerializedName("admin_ssl_cert_key_default")
    @Expose
    private String adminSslCertKeyDefault;
    @SerializedName("dns_resolver")
    @Expose
    private List<String> dnsResolver = new ArrayList<String>();
    @SerializedName("admin_port")
    @Expose
    private Double adminPort;
    @SerializedName("cassandra_repl_strategy")
    @Expose
    private String cassandraReplStrategy;
    @SerializedName("cluster_listen")
    @Expose
    private String clusterListen;
    @SerializedName("admin_ssl_cert")
    @Expose
    private String adminSslCert;
    @SerializedName("dns_hostsfile")
    @Expose
    private String dnsHostsfile;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("lua_package_path")
    @Expose
    private String luaPackagePath;
    @SerializedName("admin_ssl_cert_key")
    @Expose
    private String adminSslCertKey;
    @SerializedName("nginx_daemon")
    @Expose
    private String nginxDaemon;
    @SerializedName("custom_plugins")
    @Expose
    private List<String> customPlugins = new ArrayList<String>();
    @SerializedName("pg_ssl_verify")
    @Expose
    private Boolean pgSslVerify;
    @SerializedName("serf_pid")
    @Expose
    private String serfPid;
    @SerializedName("lua_socket_pool_size")
    @Expose
    private Double luaSocketPoolSize;
    @SerializedName("lua_code_cache")
    @Expose
    private String luaCodeCache;
    @SerializedName("cassandra_username")
    @Expose
    private String cassandraUsername;
    @SerializedName("pg_user")
    @Expose
    private String pgUser;
    @SerializedName("cassandra_timeout")
    @Expose
    private Double cassandraTimeout;
    @SerializedName("mem_cache_size")
    @Expose
    private String memCacheSize;
    @SerializedName("cassandra_ssl_verify")
    @Expose
    private Boolean cassandraSslVerify;
    @SerializedName("ssl_cert_csr_default")
    @Expose
    private String sslCertCsrDefault;
    @SerializedName("nginx_conf")
    @Expose
    private String nginxConf;
    @SerializedName("serf_node_id")
    @Expose
    private String serfNodeId;
    @SerializedName("serf_event")
    @Expose
    private String serfEvent;
    @SerializedName("admin_ssl_ip")
    @Expose
    private String adminSslIp;
    @SerializedName("admin_ssl_cert_csr_default")
    @Expose
    private String adminSslCertCsrDefault;
    @SerializedName("nginx_admin_acc_logs")
    @Expose
    private String nginxAdminAccLogs;
    @SerializedName("cluster_listen_rpc")
    @Expose
    private String clusterListenRpc;

    /**
     * 
     * @return
     *     The nginxAccLogs
     */
    public String getNginxAccLogs() {
        return nginxAccLogs;
    }

    /**
     * 
     * @param nginxAccLogs
     *     The nginx_acc_logs
     */
    public void setNginxAccLogs(String nginxAccLogs) {
        this.nginxAccLogs = nginxAccLogs;
    }

    public Configuration_ withNginxAccLogs(String nginxAccLogs) {
        this.nginxAccLogs = nginxAccLogs;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraLbPolicy
     */
    public String getCassandraLbPolicy() {
        return cassandraLbPolicy;
    }

    /**
     * 
     * @param cassandraLbPolicy
     *     The cassandra_lb_policy
     */
    public void setCassandraLbPolicy(String cassandraLbPolicy) {
        this.cassandraLbPolicy = cassandraLbPolicy;
    }

    public Configuration_ withCassandraLbPolicy(String cassandraLbPolicy) {
        this.cassandraLbPolicy = cassandraLbPolicy;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraPort
     */
    public Long getCassandraPort() {
        return cassandraPort;
    }

    /**
     * 
     * @param cassandraPort
     *     The cassandra_port
     */
    public void setCassandraPort(Long cassandraPort) {
        this.cassandraPort = cassandraPort;
    }

    public Configuration_ withCassandraPort(Long cassandraPort) {
        this.cassandraPort = cassandraPort;
        return this;
    }

    /**
     * 
     * @return
     *     The serfPath
     */
    public String getSerfPath() {
        return serfPath;
    }

    /**
     * 
     * @param serfPath
     *     The serf_path
     */
    public void setSerfPath(String serfPath) {
        this.serfPath = serfPath;
    }

    public Configuration_ withSerfPath(String serfPath) {
        this.serfPath = serfPath;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxKongConf
     */
    public String getNginxKongConf() {
        return nginxKongConf;
    }

    /**
     * 
     * @param nginxKongConf
     *     The nginx_kong_conf
     */
    public void setNginxKongConf(String nginxKongConf) {
        this.nginxKongConf = nginxKongConf;
    }

    public Configuration_ withNginxKongConf(String nginxKongConf) {
        this.nginxKongConf = nginxKongConf;
        return this;
    }

    /**
     * 
     * @return
     *     The proxyListen
     */
    public String getProxyListen() {
        return proxyListen;
    }

    /**
     * 
     * @param proxyListen
     *     The proxy_listen
     */
    public void setProxyListen(String proxyListen) {
        this.proxyListen = proxyListen;
    }

    public Configuration_ withProxyListen(String proxyListen) {
        this.proxyListen = proxyListen;
        return this;
    }

    /**
     * 
     * @return
     *     The sslCert
     */
    public String getSslCert() {
        return sslCert;
    }

    /**
     * 
     * @param sslCert
     *     The ssl_cert
     */
    public void setSslCert(String sslCert) {
        this.sslCert = sslCert;
    }

    public Configuration_ withSslCert(String sslCert) {
        this.sslCert = sslCert;
        return this;
    }

    /**
     * 
     * @return
     *     The luaSslVerifyDepth
     */
    public Long getLuaSslVerifyDepth() {
        return luaSslVerifyDepth;
    }

    /**
     * 
     * @param luaSslVerifyDepth
     *     The lua_ssl_verify_depth
     */
    public void setLuaSslVerifyDepth(Long luaSslVerifyDepth) {
        this.luaSslVerifyDepth = luaSslVerifyDepth;
    }

    public Configuration_ withLuaSslVerifyDepth(Long luaSslVerifyDepth) {
        this.luaSslVerifyDepth = luaSslVerifyDepth;
        return this;
    }

    /**
     * 
     * @return
     *     The adminListen
     */
    public String getAdminListen() {
        return adminListen;
    }

    /**
     * 
     * @param adminListen
     *     The admin_listen
     */
    public void setAdminListen(String adminListen) {
        this.adminListen = adminListen;
    }

    public Configuration_ withAdminListen(String adminListen) {
        this.adminListen = adminListen;
        return this;
    }

    /**
     * 
     * @return
     *     The adminListenSsl
     */
    public String getAdminListenSsl() {
        return adminListenSsl;
    }

    /**
     * 
     * @param adminListenSsl
     *     The admin_listen_ssl
     */
    public void setAdminListenSsl(String adminListenSsl) {
        this.adminListenSsl = adminListenSsl;
    }

    public Configuration_ withAdminListenSsl(String adminListenSsl) {
        this.adminListenSsl = adminListenSsl;
        return this;
    }

    /**
     * 
     * @return
     *     The sslCertDefault
     */
    public String getSslCertDefault() {
        return sslCertDefault;
    }

    /**
     * 
     * @param sslCertDefault
     *     The ssl_cert_default
     */
    public void setSslCertDefault(String sslCertDefault) {
        this.sslCertDefault = sslCertDefault;
    }

    public Configuration_ withSslCertDefault(String sslCertDefault) {
        this.sslCertDefault = sslCertDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraReplFactor
     */
    public Long getCassandraReplFactor() {
        return cassandraReplFactor;
    }

    /**
     * 
     * @param cassandraReplFactor
     *     The cassandra_repl_factor
     */
    public void setCassandraReplFactor(Long cassandraReplFactor) {
        this.cassandraReplFactor = cassandraReplFactor;
    }

    public Configuration_ withCassandraReplFactor(Long cassandraReplFactor) {
        this.cassandraReplFactor = cassandraReplFactor;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterProfile
     */
    public String getClusterProfile() {
        return clusterProfile;
    }

    /**
     * 
     * @param clusterProfile
     *     The cluster_profile
     */
    public void setClusterProfile(String clusterProfile) {
        this.clusterProfile = clusterProfile;
    }

    public Configuration_ withClusterProfile(String clusterProfile) {
        this.clusterProfile = clusterProfile;
        return this;
    }

    /**
     * 
     * @return
     *     The upstreamKeepalive
     */
    public Long getUpstreamKeepalive() {
        return upstreamKeepalive;
    }

    /**
     * 
     * @param upstreamKeepalive
     *     The upstream_keepalive
     */
    public void setUpstreamKeepalive(Long upstreamKeepalive) {
        this.upstreamKeepalive = upstreamKeepalive;
    }

    public Configuration_ withUpstreamKeepalive(Long upstreamKeepalive) {
        this.upstreamKeepalive = upstreamKeepalive;
        return this;
    }

    /**
     * 
     * @return
     *     The luaPackageCpath
     */
    public String getLuaPackageCpath() {
        return luaPackageCpath;
    }

    /**
     * 
     * @param luaPackageCpath
     *     The lua_package_cpath
     */
    public void setLuaPackageCpath(String luaPackageCpath) {
        this.luaPackageCpath = luaPackageCpath;
    }

    public Configuration_ withLuaPackageCpath(String luaPackageCpath) {
        this.luaPackageCpath = luaPackageCpath;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraConsistency
     */
    public String getCassandraConsistency() {
        return cassandraConsistency;
    }

    /**
     * 
     * @param cassandraConsistency
     *     The cassandra_consistency
     */
    public void setCassandraConsistency(String cassandraConsistency) {
        this.cassandraConsistency = cassandraConsistency;
    }

    public Configuration_ withCassandraConsistency(String cassandraConsistency) {
        this.cassandraConsistency = cassandraConsistency;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslCertDefault
     */
    public String getAdminSslCertDefault() {
        return adminSslCertDefault;
    }

    /**
     * 
     * @param adminSslCertDefault
     *     The admin_ssl_cert_default
     */
    public void setAdminSslCertDefault(String adminSslCertDefault) {
        this.adminSslCertDefault = adminSslCertDefault;
    }

    public Configuration_ withAdminSslCertDefault(String adminSslCertDefault) {
        this.adminSslCertDefault = adminSslCertDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The pgHost
     */
    public String getPgHost() {
        return pgHost;
    }

    /**
     * 
     * @param pgHost
     *     The pg_host
     */
    public void setPgHost(String pgHost) {
        this.pgHost = pgHost;
    }

    public Configuration_ withPgHost(String pgHost) {
        this.pgHost = pgHost;
        return this;
    }

    /**
     * 
     * @return
     *     The pgPort
     */
    public Long getPgPort() {
        return pgPort;
    }

    /**
     * 
     * @param pgPort
     *     The pg_port
     */
    public void setPgPort(Long pgPort) {
        this.pgPort = pgPort;
    }

    public Configuration_ withPgPort(Long pgPort) {
        this.pgPort = pgPort;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterTtlOnFailure
     */
    public Long getClusterTtlOnFailure() {
        return clusterTtlOnFailure;
    }

    /**
     * 
     * @param clusterTtlOnFailure
     *     The cluster_ttl_on_failure
     */
    public void setClusterTtlOnFailure(Long clusterTtlOnFailure) {
        this.clusterTtlOnFailure = clusterTtlOnFailure;
    }

    public Configuration_ withClusterTtlOnFailure(Long clusterTtlOnFailure) {
        this.clusterTtlOnFailure = clusterTtlOnFailure;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxErrLogs
     */
    public String getNginxErrLogs() {
        return nginxErrLogs;
    }

    /**
     * 
     * @param nginxErrLogs
     *     The nginx_err_logs
     */
    public void setNginxErrLogs(String nginxErrLogs) {
        this.nginxErrLogs = nginxErrLogs;
    }

    public Configuration_ withNginxErrLogs(String nginxErrLogs) {
        this.nginxErrLogs = nginxErrLogs;
        return this;
    }

    /**
     * 
     * @return
     *     The plugins
     */
    public Plugins_ getPlugins() {
        return plugins;
    }

    /**
     * 
     * @param plugins
     *     The plugins
     */
    public void setPlugins(Plugins_ plugins) {
        this.plugins = plugins;
    }

    public Configuration_ withPlugins(Plugins_ plugins) {
        this.plugins = plugins;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxOptimizations
     */
    public Boolean getNginxOptimizations() {
        return nginxOptimizations;
    }

    /**
     * 
     * @param nginxOptimizations
     *     The nginx_optimizations
     */
    public void setNginxOptimizations(Boolean nginxOptimizations) {
        this.nginxOptimizations = nginxOptimizations;
    }

    public Configuration_ withNginxOptimizations(Boolean nginxOptimizations) {
        this.nginxOptimizations = nginxOptimizations;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraKeyspace
     */
    public String getCassandraKeyspace() {
        return cassandraKeyspace;
    }

    /**
     * 
     * @param cassandraKeyspace
     *     The cassandra_keyspace
     */
    public void setCassandraKeyspace(String cassandraKeyspace) {
        this.cassandraKeyspace = cassandraKeyspace;
    }

    public Configuration_ withCassandraKeyspace(String cassandraKeyspace) {
        this.cassandraKeyspace = cassandraKeyspace;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraDataCenters
     */
    public List<String> getCassandraDataCenters() {
        return cassandraDataCenters;
    }

    /**
     * 
     * @param cassandraDataCenters
     *     The cassandra_data_centers
     */
    public void setCassandraDataCenters(List<String> cassandraDataCenters) {
        this.cassandraDataCenters = cassandraDataCenters;
    }

    public Configuration_ withCassandraDataCenters(List<String> cassandraDataCenters) {
        this.cassandraDataCenters = cassandraDataCenters;
        return this;
    }

    /**
     * 
     * @return
     *     The sslCertKey
     */
    public String getSslCertKey() {
        return sslCertKey;
    }

    /**
     * 
     * @param sslCertKey
     *     The ssl_cert_key
     */
    public void setSslCertKey(String sslCertKey) {
        this.sslCertKey = sslCertKey;
    }

    public Configuration_ withSslCertKey(String sslCertKey) {
        this.sslCertKey = sslCertKey;
        return this;
    }

    /**
     * 
     * @return
     *     The ssl
     */
    public Boolean getSsl() {
        return ssl;
    }

    /**
     * 
     * @param ssl
     *     The ssl
     */
    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Configuration_ withSsl(Boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraSsl
     */
    public Boolean getCassandraSsl() {
        return cassandraSsl;
    }

    /**
     * 
     * @param cassandraSsl
     *     The cassandra_ssl
     */
    public void setCassandraSsl(Boolean cassandraSsl) {
        this.cassandraSsl = cassandraSsl;
    }

    public Configuration_ withCassandraSsl(Boolean cassandraSsl) {
        this.cassandraSsl = cassandraSsl;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSsl
     */
    public Boolean getAdminSsl() {
        return adminSsl;
    }

    /**
     * 
     * @param adminSsl
     *     The admin_ssl
     */
    public void setAdminSsl(Boolean adminSsl) {
        this.adminSsl = adminSsl;
    }

    public Configuration_ withAdminSsl(Boolean adminSsl) {
        this.adminSsl = adminSsl;
        return this;
    }

    /**
     * 
     * @return
     *     The sslCertKeyDefault
     */
    public String getSslCertKeyDefault() {
        return sslCertKeyDefault;
    }

    /**
     * 
     * @param sslCertKeyDefault
     *     The ssl_cert_key_default
     */
    public void setSslCertKeyDefault(String sslCertKeyDefault) {
        this.sslCertKeyDefault = sslCertKeyDefault;
    }

    public Configuration_ withSslCertKeyDefault(String sslCertKeyDefault) {
        this.sslCertKeyDefault = sslCertKeyDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The anonymousReports
     */
    public Boolean getAnonymousReports() {
        return anonymousReports;
    }

    /**
     * 
     * @param anonymousReports
     *     The anonymous_reports
     */
    public void setAnonymousReports(Boolean anonymousReports) {
        this.anonymousReports = anonymousReports;
    }

    public Configuration_ withAnonymousReports(Boolean anonymousReports) {
        this.anonymousReports = anonymousReports;
        return this;
    }

    /**
     * 
     * @return
     *     The kongEnv
     */
    public String getKongEnv() {
        return kongEnv;
    }

    /**
     * 
     * @param kongEnv
     *     The kong_env
     */
    public void setKongEnv(String kongEnv) {
        this.kongEnv = kongEnv;
    }

    public Configuration_ withKongEnv(String kongEnv) {
        this.kongEnv = kongEnv;
        return this;
    }

    /**
     * 
     * @return
     *     The serfLog
     */
    public String getSerfLog() {
        return serfLog;
    }

    /**
     * 
     * @param serfLog
     *     The serf_log
     */
    public void setSerfLog(String serfLog) {
        this.serfLog = serfLog;
    }

    public Configuration_ withSerfLog(String serfLog) {
        this.serfLog = serfLog;
        return this;
    }

    /**
     * 
     * @return
     *     The logLevel
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * 
     * @param logLevel
     *     The log_level
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public Configuration_ withLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    /**
     * 
     * @return
     *     The proxyListenSsl
     */
    public String getProxyListenSsl() {
        return proxyListenSsl;
    }

    /**
     * 
     * @param proxyListenSsl
     *     The proxy_listen_ssl
     */
    public void setProxyListenSsl(String proxyListenSsl) {
        this.proxyListenSsl = proxyListenSsl;
    }

    public Configuration_ withProxyListenSsl(String proxyListenSsl) {
        this.proxyListenSsl = proxyListenSsl;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxWorkerProcesses
     */
    public String getNginxWorkerProcesses() {
        return nginxWorkerProcesses;
    }

    /**
     * 
     * @param nginxWorkerProcesses
     *     The nginx_worker_processes
     */
    public void setNginxWorkerProcesses(String nginxWorkerProcesses) {
        this.nginxWorkerProcesses = nginxWorkerProcesses;
    }

    public Configuration_ withNginxWorkerProcesses(String nginxWorkerProcesses) {
        this.nginxWorkerProcesses = nginxWorkerProcesses;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraContactPoints
     */
    public List<String> getCassandraContactPoints() {
        return cassandraContactPoints;
    }

    /**
     * 
     * @param cassandraContactPoints
     *     The cassandra_contact_points
     */
    public void setCassandraContactPoints(List<String> cassandraContactPoints) {
        this.cassandraContactPoints = cassandraContactPoints;
    }

    public Configuration_ withCassandraContactPoints(List<String> cassandraContactPoints) {
        this.cassandraContactPoints = cassandraContactPoints;
        return this;
    }

    /**
     * 
     * @return
     *     The proxyIp
     */
    public String getProxyIp() {
        return proxyIp;
    }

    /**
     * 
     * @param proxyIp
     *     The proxy_ip
     */
    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public Configuration_ withProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
        return this;
    }

    /**
     * 
     * @return
     *     The pgSsl
     */
    public Boolean getPgSsl() {
        return pgSsl;
    }

    /**
     * 
     * @param pgSsl
     *     The pg_ssl
     */
    public void setPgSsl(Boolean pgSsl) {
        this.pgSsl = pgSsl;
    }

    public Configuration_ withPgSsl(Boolean pgSsl) {
        this.pgSsl = pgSsl;
        return this;
    }

    /**
     * 
     * @return
     *     The proxySslPort
     */
    public Double getProxySslPort() {
        return proxySslPort;
    }

    /**
     * 
     * @param proxySslPort
     *     The proxy_ssl_port
     */
    public void setProxySslPort(Double proxySslPort) {
        this.proxySslPort = proxySslPort;
    }

    public Configuration_ withProxySslPort(Double proxySslPort) {
        this.proxySslPort = proxySslPort;
        return this;
    }

    /**
     * 
     * @return
     *     The pgDatabase
     */
    public String getPgDatabase() {
        return pgDatabase;
    }

    /**
     * 
     * @param pgDatabase
     *     The pg_database
     */
    public void setPgDatabase(String pgDatabase) {
        this.pgDatabase = pgDatabase;
    }

    public Configuration_ withPgDatabase(String pgDatabase) {
        this.pgDatabase = pgDatabase;
        return this;
    }

    /**
     * 
     * @return
     *     The adminIp
     */
    public String getAdminIp() {
        return adminIp;
    }

    /**
     * 
     * @param adminIp
     *     The admin_ip
     */
    public void setAdminIp(String adminIp) {
        this.adminIp = adminIp;
    }

    public Configuration_ withAdminIp(String adminIp) {
        this.adminIp = adminIp;
        return this;
    }

    /**
     * 
     * @return
     *     The database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * 
     * @param database
     *     The database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    public Configuration_ withDatabase(String database) {
        this.database = database;
        return this;
    }

    /**
     * 
     * @return
     *     The proxyPort
     */
    public Double getProxyPort() {
        return proxyPort;
    }

    /**
     * 
     * @param proxyPort
     *     The proxy_port
     */
    public void setProxyPort(Double proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Configuration_ withProxyPort(Double proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslPort
     */
    public Double getAdminSslPort() {
        return adminSslPort;
    }

    /**
     * 
     * @param adminSslPort
     *     The admin_ssl_port
     */
    public void setAdminSslPort(Double adminSslPort) {
        this.adminSslPort = adminSslPort;
    }

    public Configuration_ withAdminSslPort(Double adminSslPort) {
        this.adminSslPort = adminSslPort;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxPid
     */
    public String getNginxPid() {
        return nginxPid;
    }

    /**
     * 
     * @param nginxPid
     *     The nginx_pid
     */
    public void setNginxPid(String nginxPid) {
        this.nginxPid = nginxPid;
    }

    public Configuration_ withNginxPid(String nginxPid) {
        this.nginxPid = nginxPid;
        return this;
    }

    /**
     * 
     * @return
     *     The proxySslIp
     */
    public String getProxySslIp() {
        return proxySslIp;
    }

    /**
     * 
     * @param proxySslIp
     *     The proxy_ssl_ip
     */
    public void setProxySslIp(String proxySslIp) {
        this.proxySslIp = proxySslIp;
    }

    public Configuration_ withProxySslIp(String proxySslIp) {
        this.proxySslIp = proxySslIp;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslCertKeyDefault
     */
    public String getAdminSslCertKeyDefault() {
        return adminSslCertKeyDefault;
    }

    /**
     * 
     * @param adminSslCertKeyDefault
     *     The admin_ssl_cert_key_default
     */
    public void setAdminSslCertKeyDefault(String adminSslCertKeyDefault) {
        this.adminSslCertKeyDefault = adminSslCertKeyDefault;
    }

    public Configuration_ withAdminSslCertKeyDefault(String adminSslCertKeyDefault) {
        this.adminSslCertKeyDefault = adminSslCertKeyDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The dnsResolver
     */
    public List<String> getDnsResolver() {
        return dnsResolver;
    }

    /**
     * 
     * @param dnsResolver
     *     The dns_resolver
     */
    public void setDnsResolver(List<String> dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    public Configuration_ withDnsResolver(List<String> dnsResolver) {
        this.dnsResolver = dnsResolver;
        return this;
    }

    /**
     * 
     * @return
     *     The adminPort
     */
    public Double getAdminPort() {
        return adminPort;
    }

    /**
     * 
     * @param adminPort
     *     The admin_port
     */
    public void setAdminPort(Double adminPort) {
        this.adminPort = adminPort;
    }

    public Configuration_ withAdminPort(Double adminPort) {
        this.adminPort = adminPort;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraReplStrategy
     */
    public String getCassandraReplStrategy() {
        return cassandraReplStrategy;
    }

    /**
     * 
     * @param cassandraReplStrategy
     *     The cassandra_repl_strategy
     */
    public void setCassandraReplStrategy(String cassandraReplStrategy) {
        this.cassandraReplStrategy = cassandraReplStrategy;
    }

    public Configuration_ withCassandraReplStrategy(String cassandraReplStrategy) {
        this.cassandraReplStrategy = cassandraReplStrategy;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterListen
     */
    public String getClusterListen() {
        return clusterListen;
    }

    /**
     * 
     * @param clusterListen
     *     The cluster_listen
     */
    public void setClusterListen(String clusterListen) {
        this.clusterListen = clusterListen;
    }

    public Configuration_ withClusterListen(String clusterListen) {
        this.clusterListen = clusterListen;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslCert
     */
    public String getAdminSslCert() {
        return adminSslCert;
    }

    /**
     * 
     * @param adminSslCert
     *     The admin_ssl_cert
     */
    public void setAdminSslCert(String adminSslCert) {
        this.adminSslCert = adminSslCert;
    }

    public Configuration_ withAdminSslCert(String adminSslCert) {
        this.adminSslCert = adminSslCert;
        return this;
    }

    /**
     * 
     * @return
     *     The dnsHostsfile
     */
    public String getDnsHostsfile() {
        return dnsHostsfile;
    }

    /**
     * 
     * @param dnsHostsfile
     *     The dns_hostsfile
     */
    public void setDnsHostsfile(String dnsHostsfile) {
        this.dnsHostsfile = dnsHostsfile;
    }

    public Configuration_ withDnsHostsfile(String dnsHostsfile) {
        this.dnsHostsfile = dnsHostsfile;
        return this;
    }

    /**
     * 
     * @return
     *     The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 
     * @param prefix
     *     The prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Configuration_ withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 
     * @return
     *     The luaPackagePath
     */
    public String getLuaPackagePath() {
        return luaPackagePath;
    }

    /**
     * 
     * @param luaPackagePath
     *     The lua_package_path
     */
    public void setLuaPackagePath(String luaPackagePath) {
        this.luaPackagePath = luaPackagePath;
    }

    public Configuration_ withLuaPackagePath(String luaPackagePath) {
        this.luaPackagePath = luaPackagePath;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslCertKey
     */
    public String getAdminSslCertKey() {
        return adminSslCertKey;
    }

    /**
     * 
     * @param adminSslCertKey
     *     The admin_ssl_cert_key
     */
    public void setAdminSslCertKey(String adminSslCertKey) {
        this.adminSslCertKey = adminSslCertKey;
    }

    public Configuration_ withAdminSslCertKey(String adminSslCertKey) {
        this.adminSslCertKey = adminSslCertKey;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxDaemon
     */
    public String getNginxDaemon() {
        return nginxDaemon;
    }

    /**
     * 
     * @param nginxDaemon
     *     The nginx_daemon
     */
    public void setNginxDaemon(String nginxDaemon) {
        this.nginxDaemon = nginxDaemon;
    }

    public Configuration_ withNginxDaemon(String nginxDaemon) {
        this.nginxDaemon = nginxDaemon;
        return this;
    }

    /**
     * 
     * @return
     *     The customPlugins
     */
    public List<String> getCustomPlugins() {
        return customPlugins;
    }

    /**
     * 
     * @param customPlugins
     *     The custom_plugins
     */
    public void setCustomPlugins(List<String> customPlugins) {
        this.customPlugins = customPlugins;
    }

    public Configuration_ withCustomPlugins(List<String> customPlugins) {
        this.customPlugins = customPlugins;
        return this;
    }

    /**
     * 
     * @return
     *     The pgSslVerify
     */
    public Boolean getPgSslVerify() {
        return pgSslVerify;
    }

    /**
     * 
     * @param pgSslVerify
     *     The pg_ssl_verify
     */
    public void setPgSslVerify(Boolean pgSslVerify) {
        this.pgSslVerify = pgSslVerify;
    }

    public Configuration_ withPgSslVerify(Boolean pgSslVerify) {
        this.pgSslVerify = pgSslVerify;
        return this;
    }

    /**
     * 
     * @return
     *     The serfPid
     */
    public String getSerfPid() {
        return serfPid;
    }

    /**
     * 
     * @param serfPid
     *     The serf_pid
     */
    public void setSerfPid(String serfPid) {
        this.serfPid = serfPid;
    }

    public Configuration_ withSerfPid(String serfPid) {
        this.serfPid = serfPid;
        return this;
    }

    /**
     * 
     * @return
     *     The luaSocketPoolSize
     */
    public Double getLuaSocketPoolSize() {
        return luaSocketPoolSize;
    }

    /**
     * 
     * @param luaSocketPoolSize
     *     The lua_socket_pool_size
     */
    public void setLuaSocketPoolSize(Double luaSocketPoolSize) {
        this.luaSocketPoolSize = luaSocketPoolSize;
    }

    public Configuration_ withLuaSocketPoolSize(Double luaSocketPoolSize) {
        this.luaSocketPoolSize = luaSocketPoolSize;
        return this;
    }

    /**
     * 
     * @return
     *     The luaCodeCache
     */
    public String getLuaCodeCache() {
        return luaCodeCache;
    }

    /**
     * 
     * @param luaCodeCache
     *     The lua_code_cache
     */
    public void setLuaCodeCache(String luaCodeCache) {
        this.luaCodeCache = luaCodeCache;
    }

    public Configuration_ withLuaCodeCache(String luaCodeCache) {
        this.luaCodeCache = luaCodeCache;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraUsername
     */
    public String getCassandraUsername() {
        return cassandraUsername;
    }

    /**
     * 
     * @param cassandraUsername
     *     The cassandra_username
     */
    public void setCassandraUsername(String cassandraUsername) {
        this.cassandraUsername = cassandraUsername;
    }

    public Configuration_ withCassandraUsername(String cassandraUsername) {
        this.cassandraUsername = cassandraUsername;
        return this;
    }

    /**
     * 
     * @return
     *     The pgUser
     */
    public String getPgUser() {
        return pgUser;
    }

    /**
     * 
     * @param pgUser
     *     The pg_user
     */
    public void setPgUser(String pgUser) {
        this.pgUser = pgUser;
    }

    public Configuration_ withPgUser(String pgUser) {
        this.pgUser = pgUser;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraTimeout
     */
    public Double getCassandraTimeout() {
        return cassandraTimeout;
    }

    /**
     * 
     * @param cassandraTimeout
     *     The cassandra_timeout
     */
    public void setCassandraTimeout(Double cassandraTimeout) {
        this.cassandraTimeout = cassandraTimeout;
    }

    public Configuration_ withCassandraTimeout(Double cassandraTimeout) {
        this.cassandraTimeout = cassandraTimeout;
        return this;
    }

    /**
     * 
     * @return
     *     The memCacheSize
     */
    public String getMemCacheSize() {
        return memCacheSize;
    }

    /**
     * 
     * @param memCacheSize
     *     The mem_cache_size
     */
    public void setMemCacheSize(String memCacheSize) {
        this.memCacheSize = memCacheSize;
    }

    public Configuration_ withMemCacheSize(String memCacheSize) {
        this.memCacheSize = memCacheSize;
        return this;
    }

    /**
     * 
     * @return
     *     The cassandraSslVerify
     */
    public Boolean getCassandraSslVerify() {
        return cassandraSslVerify;
    }

    /**
     * 
     * @param cassandraSslVerify
     *     The cassandra_ssl_verify
     */
    public void setCassandraSslVerify(Boolean cassandraSslVerify) {
        this.cassandraSslVerify = cassandraSslVerify;
    }

    public Configuration_ withCassandraSslVerify(Boolean cassandraSslVerify) {
        this.cassandraSslVerify = cassandraSslVerify;
        return this;
    }

    /**
     * 
     * @return
     *     The sslCertCsrDefault
     */
    public String getSslCertCsrDefault() {
        return sslCertCsrDefault;
    }

    /**
     * 
     * @param sslCertCsrDefault
     *     The ssl_cert_csr_default
     */
    public void setSslCertCsrDefault(String sslCertCsrDefault) {
        this.sslCertCsrDefault = sslCertCsrDefault;
    }

    public Configuration_ withSslCertCsrDefault(String sslCertCsrDefault) {
        this.sslCertCsrDefault = sslCertCsrDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxConf
     */
    public String getNginxConf() {
        return nginxConf;
    }

    /**
     * 
     * @param nginxConf
     *     The nginx_conf
     */
    public void setNginxConf(String nginxConf) {
        this.nginxConf = nginxConf;
    }

    public Configuration_ withNginxConf(String nginxConf) {
        this.nginxConf = nginxConf;
        return this;
    }

    /**
     * 
     * @return
     *     The serfNodeId
     */
    public String getSerfNodeId() {
        return serfNodeId;
    }

    /**
     * 
     * @param serfNodeId
     *     The serf_node_id
     */
    public void setSerfNodeId(String serfNodeId) {
        this.serfNodeId = serfNodeId;
    }

    public Configuration_ withSerfNodeId(String serfNodeId) {
        this.serfNodeId = serfNodeId;
        return this;
    }

    /**
     * 
     * @return
     *     The serfEvent
     */
    public String getSerfEvent() {
        return serfEvent;
    }

    /**
     * 
     * @param serfEvent
     *     The serf_event
     */
    public void setSerfEvent(String serfEvent) {
        this.serfEvent = serfEvent;
    }

    public Configuration_ withSerfEvent(String serfEvent) {
        this.serfEvent = serfEvent;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslIp
     */
    public String getAdminSslIp() {
        return adminSslIp;
    }

    /**
     * 
     * @param adminSslIp
     *     The admin_ssl_ip
     */
    public void setAdminSslIp(String adminSslIp) {
        this.adminSslIp = adminSslIp;
    }

    public Configuration_ withAdminSslIp(String adminSslIp) {
        this.adminSslIp = adminSslIp;
        return this;
    }

    /**
     * 
     * @return
     *     The adminSslCertCsrDefault
     */
    public String getAdminSslCertCsrDefault() {
        return adminSslCertCsrDefault;
    }

    /**
     * 
     * @param adminSslCertCsrDefault
     *     The admin_ssl_cert_csr_default
     */
    public void setAdminSslCertCsrDefault(String adminSslCertCsrDefault) {
        this.adminSslCertCsrDefault = adminSslCertCsrDefault;
    }

    public Configuration_ withAdminSslCertCsrDefault(String adminSslCertCsrDefault) {
        this.adminSslCertCsrDefault = adminSslCertCsrDefault;
        return this;
    }

    /**
     * 
     * @return
     *     The nginxAdminAccLogs
     */
    public String getNginxAdminAccLogs() {
        return nginxAdminAccLogs;
    }

    /**
     * 
     * @param nginxAdminAccLogs
     *     The nginx_admin_acc_logs
     */
    public void setNginxAdminAccLogs(String nginxAdminAccLogs) {
        this.nginxAdminAccLogs = nginxAdminAccLogs;
    }

    public Configuration_ withNginxAdminAccLogs(String nginxAdminAccLogs) {
        this.nginxAdminAccLogs = nginxAdminAccLogs;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterListenRpc
     */
    public String getClusterListenRpc() {
        return clusterListenRpc;
    }

    /**
     * 
     * @param clusterListenRpc
     *     The cluster_listen_rpc
     */
    public void setClusterListenRpc(String clusterListenRpc) {
        this.clusterListenRpc = clusterListenRpc;
    }

    public Configuration_ withClusterListenRpc(String clusterListenRpc) {
        this.clusterListenRpc = clusterListenRpc;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nginxAccLogs).append(cassandraLbPolicy).append(cassandraPort).append(serfPath).append(nginxKongConf).append(proxyListen).append(sslCert).append(luaSslVerifyDepth).append(adminListen).append(adminListenSsl).append(sslCertDefault).append(cassandraReplFactor).append(clusterProfile).append(upstreamKeepalive).append(luaPackageCpath).append(cassandraConsistency).append(adminSslCertDefault).append(pgHost).append(pgPort).append(clusterTtlOnFailure).append(nginxErrLogs).append(plugins).append(nginxOptimizations).append(cassandraKeyspace).append(cassandraDataCenters).append(sslCertKey).append(ssl).append(cassandraSsl).append(adminSsl).append(sslCertKeyDefault).append(anonymousReports).append(kongEnv).append(serfLog).append(logLevel).append(proxyListenSsl).append(nginxWorkerProcesses).append(cassandraContactPoints).append(proxyIp).append(pgSsl).append(proxySslPort).append(pgDatabase).append(adminIp).append(database).append(proxyPort).append(adminSslPort).append(nginxPid).append(proxySslIp).append(adminSslCertKeyDefault).append(dnsResolver).append(adminPort).append(cassandraReplStrategy).append(clusterListen).append(adminSslCert).append(dnsHostsfile).append(prefix).append(luaPackagePath).append(adminSslCertKey).append(nginxDaemon).append(customPlugins).append(pgSslVerify).append(serfPid).append(luaSocketPoolSize).append(luaCodeCache).append(cassandraUsername).append(pgUser).append(cassandraTimeout).append(memCacheSize).append(cassandraSslVerify).append(sslCertCsrDefault).append(nginxConf).append(serfNodeId).append(serfEvent).append(adminSslIp).append(adminSslCertCsrDefault).append(nginxAdminAccLogs).append(clusterListenRpc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Configuration_) == false) {
            return false;
        }
        Configuration_ rhs = ((Configuration_) other);
        return new EqualsBuilder().append(nginxAccLogs, rhs.nginxAccLogs).append(cassandraLbPolicy, rhs.cassandraLbPolicy).append(cassandraPort, rhs.cassandraPort).append(serfPath, rhs.serfPath).append(nginxKongConf, rhs.nginxKongConf).append(proxyListen, rhs.proxyListen).append(sslCert, rhs.sslCert).append(luaSslVerifyDepth, rhs.luaSslVerifyDepth).append(adminListen, rhs.adminListen).append(adminListenSsl, rhs.adminListenSsl).append(sslCertDefault, rhs.sslCertDefault).append(cassandraReplFactor, rhs.cassandraReplFactor).append(clusterProfile, rhs.clusterProfile).append(upstreamKeepalive, rhs.upstreamKeepalive).append(luaPackageCpath, rhs.luaPackageCpath).append(cassandraConsistency, rhs.cassandraConsistency).append(adminSslCertDefault, rhs.adminSslCertDefault).append(pgHost, rhs.pgHost).append(pgPort, rhs.pgPort).append(clusterTtlOnFailure, rhs.clusterTtlOnFailure).append(nginxErrLogs, rhs.nginxErrLogs).append(plugins, rhs.plugins).append(nginxOptimizations, rhs.nginxOptimizations).append(cassandraKeyspace, rhs.cassandraKeyspace).append(cassandraDataCenters, rhs.cassandraDataCenters).append(sslCertKey, rhs.sslCertKey).append(ssl, rhs.ssl).append(cassandraSsl, rhs.cassandraSsl).append(adminSsl, rhs.adminSsl).append(sslCertKeyDefault, rhs.sslCertKeyDefault).append(anonymousReports, rhs.anonymousReports).append(kongEnv, rhs.kongEnv).append(serfLog, rhs.serfLog).append(logLevel, rhs.logLevel).append(proxyListenSsl, rhs.proxyListenSsl).append(nginxWorkerProcesses, rhs.nginxWorkerProcesses).append(cassandraContactPoints, rhs.cassandraContactPoints).append(proxyIp, rhs.proxyIp).append(pgSsl, rhs.pgSsl).append(proxySslPort, rhs.proxySslPort).append(pgDatabase, rhs.pgDatabase).append(adminIp, rhs.adminIp).append(database, rhs.database).append(proxyPort, rhs.proxyPort).append(adminSslPort, rhs.adminSslPort).append(nginxPid, rhs.nginxPid).append(proxySslIp, rhs.proxySslIp).append(adminSslCertKeyDefault, rhs.adminSslCertKeyDefault).append(dnsResolver, rhs.dnsResolver).append(adminPort, rhs.adminPort).append(cassandraReplStrategy, rhs.cassandraReplStrategy).append(clusterListen, rhs.clusterListen).append(adminSslCert, rhs.adminSslCert).append(dnsHostsfile, rhs.dnsHostsfile).append(prefix, rhs.prefix).append(luaPackagePath, rhs.luaPackagePath).append(adminSslCertKey, rhs.adminSslCertKey).append(nginxDaemon, rhs.nginxDaemon).append(customPlugins, rhs.customPlugins).append(pgSslVerify, rhs.pgSslVerify).append(serfPid, rhs.serfPid).append(luaSocketPoolSize, rhs.luaSocketPoolSize).append(luaCodeCache, rhs.luaCodeCache).append(cassandraUsername, rhs.cassandraUsername).append(pgUser, rhs.pgUser).append(cassandraTimeout, rhs.cassandraTimeout).append(memCacheSize, rhs.memCacheSize).append(cassandraSslVerify, rhs.cassandraSslVerify).append(sslCertCsrDefault, rhs.sslCertCsrDefault).append(nginxConf, rhs.nginxConf).append(serfNodeId, rhs.serfNodeId).append(serfEvent, rhs.serfEvent).append(adminSslIp, rhs.adminSslIp).append(adminSslCertCsrDefault, rhs.adminSslCertCsrDefault).append(nginxAdminAccLogs, rhs.nginxAdminAccLogs).append(clusterListenRpc, rhs.clusterListenRpc).isEquals();
    }

}
