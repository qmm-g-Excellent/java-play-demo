package cn.com.play.controllers;

import cn.com.play.controllers.config.redis.RedisCache;
import play.mvc.Controller;
import play.mvc.Result;

public class OrderController extends Controller {
    public Result apply() {
        System.out.println("------------------");
        RedisCache.getInstance();
        RedisCache.getInstance().setCache("dog", "cat");
        System.out.println(RedisCache.getInstance().getCache("dog")+"===================");
        return ok();
    }
}
