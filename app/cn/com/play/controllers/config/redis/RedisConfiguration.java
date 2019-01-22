package cn.com.play.controllers.config.redis;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import lombok.ToString;

import javax.inject.Inject;

@ToString
@Singleton
public class RedisConfiguration {
    private String host;
    private String cacheClientName;
    private int port;
    private int cacheDatabase;
    private int sessionDatabase;
    private int cacheMaxTotal;
    private int cacheMaxIdle;
    private int cacheTimeOut;
    private boolean cacheEnable;
    private boolean cacheUpdateAsync;
    private static RedisConfiguration redisConfiguration;
    private String masterName;
    private boolean sentinelEnable;
    private String password;

    @Inject
    public RedisConfiguration(Config configuration) {
        Config cache = configuration.getConfig("redis").resolve();
        host = cache.getString("host");
        port = cache.getInt("port");
        cacheEnable = cache.getBoolean("cacheEnable");
        sentinelEnable = cache.getBoolean("sentinelEnable");
        sessionDatabase = cache.getInt("sessionDatabase");
        cacheDatabase = cache.getInt("cacheDatabase");
        cacheMaxTotal = cache.getInt("cacheMaxTotal");
        cacheMaxIdle = cache.getInt("cacheMaxIdle");
        cacheTimeOut = cache.getInt("cacheTimeOut");
        cacheClientName = cache.getString("cacheClientName");
        cacheUpdateAsync = cache.getBoolean("cacheUpdateAsync");
        masterName = cache.getString("masterName");
        password = cache.getString("password");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMasterName() {
        return masterName;
    }

    public int getCacheDatabase() {
        return cacheDatabase;
    }

    public int getSessionDatabase() {
        return sessionDatabase;
    }

    public int getCacheMaxTotal() {
        return cacheMaxTotal;
    }

    public int getCacheMaxIdle() {
        return cacheMaxIdle;
    }

    public int getCacheTimeOut() {
        return cacheTimeOut;
    }

    public String getCacheClientName() {
        return cacheClientName;
    }

    public boolean isSentinelEnable() {
        return sentinelEnable;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public boolean isCacheUpdateAsync() {
        return cacheUpdateAsync;
    }

    public static void setInstance(RedisConfiguration configuration) {
        redisConfiguration = configuration;
    }

    public String getPassword() { return password; }

    public static RedisConfiguration getInstance() {
        return redisConfiguration;
    }

}
