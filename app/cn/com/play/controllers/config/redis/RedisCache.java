package cn.com.play.controllers.config.redis;

import cn.com.play.controllers.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Singleton
public class RedisCache {

    private static RedisCache instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    private final Pool<Jedis> cachePool;
    private final Pool<Jedis> sessionPool;
    private RedisConfiguration RedisConfiguration;

    @Inject
    public RedisCache (RedisConfiguration RedisConfiguration) {
        this.RedisConfiguration = RedisConfiguration;
        log.info("Get RedisConfiguration {}.",  RedisConfiguration);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(RedisConfiguration.getCacheMaxTotal());
        poolConfig.setMaxIdle(RedisConfiguration.getCacheMaxIdle());

        cachePool = new JedisPool(poolConfig,
                RedisConfiguration.getHost(),
                RedisConfiguration.getPort(),
                RedisConfiguration.getCacheTimeOut(),
                RedisConfiguration.getPassword(),
                RedisConfiguration.getCacheDatabase(),
                RedisConfiguration.getCacheClientName());

        sessionPool = new JedisPool(poolConfig,
                RedisConfiguration.getHost(),
                RedisConfiguration.getPort(),
                RedisConfiguration.getCacheTimeOut(),
                RedisConfiguration.getPassword(),
                RedisConfiguration.getSessionDatabase(),
                RedisConfiguration.getCacheClientName());
    }

    public <T> T withCacheConnection (Function<Jedis, T> consumer) {
        try (Jedis jedis = cachePool.getResource()) {
            return consumer.apply(jedis);
        } catch (Exception e) {
            LOGGER.error("withCacheConnection exception: ", e);
            return null;
        }
    }

    public <T> CompletableFuture<T> withAsyncCacheConnection (Function<Jedis, T> consumer) {
        if (RedisConfiguration.isCacheUpdateAsync()) {
            return CompletableFuture.supplyAsync(() -> withCacheConnection(consumer));
        }
        return CompletableFuture.completedFuture(withCacheConnection(consumer));
    }

    public <T> T withSessionConnection (Function<Jedis, T> consumer) {
        try (Jedis jedis = sessionPool.getResource()) {
            return consumer.apply(jedis);
        } catch (Exception e) {
            LOGGER.error("withSessionConnection exception: ", e);
            return null;
        }
    }

    public <T> CompletableFuture<T> withAsyncSessionConnection (Function<Jedis, T> consumer) {
        if (RedisConfiguration.isCacheUpdateAsync()) {
            return CompletableFuture.supplyAsync(() -> withSessionConnection(consumer));
        }
        return CompletableFuture.completedFuture(withSessionConnection(consumer));
    }

    public String getSession (String key) {
        return withSessionConnection(client -> client.get(key));
    }

    public void setSession (String key, String value, int expireSeconds) {
        withSessionConnection(client -> client.setex(key, expireSeconds, value));
    }

    public void setSessionExpire (String key, int expireSeconds) {
        withAsyncSessionConnection(client -> client.expire(key, expireSeconds));
    }

    public void delSession (String key) {
        withAsyncSessionConnection(client -> client.del(key));
    }

    public long getSessionTTL (String key) {
        return withSessionConnection(client -> client.ttl(key));
    }

    public String getCache (String key) {
        return withCacheConnection(client -> client.get(key));
    }

    public String setCache (String key, String value) {
        return withCacheConnection(client -> client.set(key, value));
    }

    public void delCache (String key) {
        withCacheConnection(client -> client.del(key));
    }

    public long getNextTradeNumber () {
        return withSessionConnection(client -> client.incr("TradeNumber"));
    }

    public String hget (String key, String field) {
        return withCacheConnection(client -> client.hget(key, field));
    }

    public Map<String, String> hgetAll (String key) {
        return withCacheConnection(client -> client.hgetAll(key));
    }

    public String flushCacheDb () {
        return withCacheConnection(Jedis::flushDB);
    }

    public static <T> PagedListVO<T> readPagedListVOFromCacheOrElse (String key, String field, Class<T> clazz,
                                                                     Supplier<PagedListVO<T>> elseFunction) {
        RedisCache redisCache = RedisCache.getInstance();
        String jsonResult = redisCache.withCacheConnection(client -> client.hget(key, field));
        if (jsonResult == null) {
            PagedListVO<T> res = elseFunction.get();
            redisCache.withAsyncCacheConnection(asyncClient -> asyncClient.hset(key, field, Json.toJson(res).toString()));
            return res;
        }
        try {
            return JsonUtil.getPagedListVOFromResult(jsonResult, clazz);
        } catch (IOException ex) {
            LOGGER.error("error on set cache.", ex);
            return new PagedListVO<>();
        }
    }

    public static <T> Set<T> readSetFromCacheOrElse (String key, String field, Class<T> clazz, Supplier<Set<T>> elseFunction) {
        RedisCache redisCache = RedisCache.getInstance();
        String jsonResult = redisCache.withCacheConnection(client -> client.hget(key, field));
        if (jsonResult == null) {
            Set<T> res = elseFunction.get();
            redisCache.withAsyncCacheConnection(asyncClient -> asyncClient.hset(key, field, Json.toJson(res).toString()));
            return res;
        }
        try {
            return JsonUtil.getValueAsSet(jsonResult, clazz);
        } catch (IOException ex) {
            LOGGER.error("error on set cache.", ex);
            return new HashSet<>();
        }
    }

    public static <T> List<T> readListFromCacheOrElse (String key, String field, Class<T> clazz, Supplier<List<T>> elseFunction) {
        RedisCache redisCache = RedisCache.getInstance();
        String jsonResult = redisCache.withCacheConnection(client -> client.hget(key, field));
        if (jsonResult == null) {
            List<T> res = elseFunction.get();
            redisCache.withAsyncCacheConnection(asyncClient -> asyncClient.hset(key, field, Json.toJson(res).toString()));
            return res;
        }
        try {
            return JsonUtil.getValueAsList(jsonResult, clazz);
        } catch (IOException ex) {
            LOGGER.error("error on set cache.", ex);
            return new ArrayList<>();
        }
    }

    public static void setInstance (RedisCache redisCache) {
        instance = redisCache;
    }

    public static RedisCache getInstance () {
        return instance;
    }

    public void destroy () {
        cachePool.destroy();
        sessionPool.destroy();
    }

}
