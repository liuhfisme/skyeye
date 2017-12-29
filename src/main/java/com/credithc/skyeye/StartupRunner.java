package com.credithc.skyeye;

import com.credithc.skyeye.bean.conf.ApplicationConf;
import com.credithc.skyeye.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @Description : 容器启动时执行一系列的操作
 * @Author ：dongbin
 * @Date ：2017/6/8
 */
@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void run(String... strings) throws Exception {
        try {
            logger.debug("StartupRunner ......");
            /*
              初始化StringRedisTemplate对象
             */
            RedisUtil.setStringRedisTemplate(stringRedisTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StartupRunner error", e);
        }
    }


}