package com.credithc.skyeye.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.credithc.skyeye.bean.SubscribeMessage;
import com.credithc.skyeye.bean.conf.ApplicationConf;
import com.credithc.skyeye.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * redis配置.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/9/19
 */
@Configuration
public class RedisConfig {
    private final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Resource
    private ApplicationConf applicationConf;

    /**
     * redis连接工厂
     * @return redis连接工程实例
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        logger.debug("RedisConnectionFactory create...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(applicationConf.getRedisPoolMaxActive());
        poolConfig.setMaxIdle(applicationConf.getRedisPoolMaxIdle());
        poolConfig.setMaxWaitMillis(applicationConf.getRedisPoolMaxWait());
        //spring boot没有提供testOnBorrow、testOnCreate、testWhileIdle，在此手动配置
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(applicationConf.getRedisHost());
        if(null != applicationConf.getRedisPassword()){
            jedisConnectionFactory.setPassword(applicationConf.getRedisPassword());
        }
        jedisConnectionFactory.setPort(applicationConf.getRedisPort());
        return jedisConnectionFactory;
    }

    /**
     * 定义StringRedisTemplate对象
     *
     * @param redisConnectionFactory redis连接的工厂类
     * @return redis模板对象
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        logger.debug("StringRedisTemplate init...");
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * 配置监听的频道和处理消息的订阅者
     *
     * @param redisConnectionFactory redis连接工厂
     * @param messageListenerAdapter 消息监听
     * @return 监听容器
     */
    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        ChannelTopic channelTopic = new ChannelTopic(RedisUtil.SKYEYE_WEBSOCKET_CHANNEL);
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(SimpMessagingTemplate simpMessagingTemplate) {
        return new MessageListenerAdapter(new RedisReceiver(simpMessagingTemplate));
    }

    /**
     * redis订阅接收者
     */
    private class RedisReceiver {
        private final Logger logger = LoggerFactory.getLogger(RedisReceiver.class);
        private SimpMessagingTemplate simpMessagingTemplate;

        public RedisReceiver(SimpMessagingTemplate simpMessagingTemplate){
            this.simpMessagingTemplate=simpMessagingTemplate;
        }
        /**
         * 接收消息
         *
         * @param message 消息体
         */
        public void handleMessage(String message) {
            logger.info("receiveMessage:"+message);
            if(StringUtils.isNotBlank(message)){
                SubscribeMessage subscribeMessage=JSON.parseObject(message, SubscribeMessage.class);
                this.simpMessagingTemplate.convertAndSendToUser(subscribeMessage.getClientId(),subscribeMessage.getDestination(),subscribeMessage.getPayload());
            }
        }
    }
}
