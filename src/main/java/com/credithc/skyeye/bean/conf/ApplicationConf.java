package com.credithc.skyeye.bean.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description :封装应用配置
 * @Author ：dongbin
 * @Date ：2017/6/12
 */
@Component
public class ApplicationConf {


    /**
     * redis配置
     */
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.pool.max-active}")
    private Integer redisPoolMaxActive;
    @Value("${spring.redis.pool.max-wait}")
    private Integer redisPoolMaxWait;
    @Value("${spring.redis.pool.max-idle}")
    private Integer redisPoolMaxIdle;




    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisPoolMaxActive() {
        return redisPoolMaxActive;
    }

    public void setRedisPoolMaxActive(Integer redisPoolMaxActive) {
        this.redisPoolMaxActive = redisPoolMaxActive;
    }

    public Integer getRedisPoolMaxWait() {
        return redisPoolMaxWait;
    }

    public void setRedisPoolMaxWait(Integer redisPoolMaxWait) {
        this.redisPoolMaxWait = redisPoolMaxWait;
    }

    public Integer getRedisPoolMaxIdle() {
        return redisPoolMaxIdle;
    }

    public void setRedisPoolMaxIdle(Integer redisPoolMaxIdle) {
        this.redisPoolMaxIdle = redisPoolMaxIdle;
    }


}
