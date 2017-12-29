package com.credithc.skyeye.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description : 缓存配置
 * @Author ：dongbin
 * @Date ：2017/6/20
 */
@Configuration
@EnableCaching
public class CachingConfig {


    /**
     * 定义一个缓存管理器
     * @return 初始化后的缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        RedisCacheManager redisCacheManager=new RedisCacheManager(redisTemplate);
        return redisCacheManager;
    }
//    @Bean
//    public CacheManager cacheManager(){
//        return new ConcurrentMapCacheManager();
//    }
}
