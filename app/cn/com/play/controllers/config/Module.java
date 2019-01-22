package cn.com.play.controllers.config;

import cn.com.play.controllers.config.redis.InitRedis;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {
        bind(InitRedis.class).asEagerSingleton();
    }

}
