package cn.com.play.controllers.config.redis;

import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class InitRedis {

    @Inject
    public InitRedis(RedisCache cache,
                     RedisConfiguration redisConfiguration,
                     ApplicationLifecycle appLifecycle) {
        RedisCache.setInstance(cache);
        RedisConfiguration.setInstance(redisConfiguration);

        appLifecycle.addStopHook(() -> {
            RedisCache.getInstance().destroy();
            return CompletableFuture.completedFuture(null);
        });
    }
}
