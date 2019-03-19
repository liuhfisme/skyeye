package com.credithc.skyeye.bean.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 封装应用配置.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/12
 */
@Component
@Data
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

}
