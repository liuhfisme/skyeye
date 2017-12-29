package com.credithc.skyeye.util;

import com.alibaba.fastjson.JSONObject;
import com.credithc.skyeye.bean.SubscribeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redis工具类
 * @Author ：dongbin
 * @Date ：2017/6/8
 */
public class RedisUtil {
	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	/**
	 * websocket订阅的频道
	 */
	public static final String SKYEYE_WEBSOCKET_CHANNEL="SKYEYE_WEBSOCKET_CHANNEL";

	private static StringRedisTemplate redisTemplate;

	public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate){
		redisTemplate=stringRedisTemplate;
	}

	/**
	 * 字符串操作。设值
	 * @param key  key
	 * @param value value
	 */
	public static void set(String key, String value){
		BoundValueOperations valueOperations= redisTemplate.boundValueOps(key);
		valueOperations.set(value);
	}

	/**
	 * 字符串操作。取值
	 * @param key key
	 * @return value
	 */
	public static String get(String key){
		BoundValueOperations valueOperations=redisTemplate.boundValueOps(key);
		Object resultVal=valueOperations.get();
		if(resultVal!=null){
			return String.valueOf(resultVal);
		}else{
			return null;
		}
	}
	/**
	 * 字符串操作，设值的同时设置过期时间
	 * @param key key值
	 * @param seconds 过期时间，单位秒
	 * @param value  value值
	 */
	public static void setex(String key, int seconds, String value){
		BoundValueOperations valueOperations=redisTemplate.boundValueOps(key);
		valueOperations.set(value,seconds, TimeUnit.SECONDS);
	}
	/**
	 * 数值增减
	 * @param key key
	 * @param incrNum 增、减数值，正数增加、负数减少
	 * @return key对应值的增减结果
	 */
	public static long incrBy(String key, long incrNum){
		BoundValueOperations valueOperations=redisTemplate.boundValueOps(key);
		return valueOperations.increment(incrNum);
	}

	/**
	 * list的长度
	 * @param key key
	 * @return key值对应的list容量
	 */
	public static long llen(String key){
		BoundListOperations listOperations=redisTemplate.boundListOps(key);
		return listOperations.size();
	}

	/**
	 * list lpush操作
	 * @param key list的key值
	 * @param value value
	 * @return 修改的记录数
	 */
	public static long lpush(String key,String value){
		BoundListOperations listOperations=redisTemplate.boundListOps(key);
		return listOperations.leftPush(value);
	}

	/**
	 * 取出list集合
	 * @param key list的key值
	 * @return 值的集合
	 */
	public  static List<Object> lrang(String key){
		BoundListOperations listOperations=redisTemplate.boundListOps(key);
		return listOperations.range(0,-1);
	}
	/**
	 * 判断list中是否存在该值
	 * @param key list的key值
	 * @param value 需要验证的value值
	 * @return true 存在
	 */
	public  static boolean lexists(String key,String value){
		if(redisTemplate.hasKey(key)){
			List list=lrang(key);
			if(list.contains(value)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * list remove操作
	 * @param key  list的key值
	 * @param value 要删除的value值
	 * @return 删除的记录数
	 */
	public static long lrem(String key,String value){
		BoundListOperations listOperations=redisTemplate.boundListOps(key);
		/*
			第一个参数为0，表示移除表中所有与 value 相等的值
		 */
		return listOperations.remove(0,value);
	}
	/**
	 * list lpop操作
	 * @param key list的key值
	 * @return 队列中获取的对象
	 */
	public static Object rpop(String key){
		BoundListOperations listOperations=redisTemplate.boundListOps(key);
		return listOperations.rightPop();
	}
	/**
	 * 删除
	 * @param key key
	 */
	public static void del(String key){
		redisTemplate.delete(key);
	}

	/**
	 * 批量删除
	 * @param keySet key集合
	 */
	public static void del(Set<String> keySet){
		redisTemplate.delete(keySet);
	}
	/**
	 * 正则匹配出所有key值
	 * @param pattern 正则
	 * @return key集合
	 */
	public static Set<String> keys(String pattern){
		return redisTemplate.keys(pattern);
	}
	/**
	 * 判断key值是否存在
	 * @param key key
	 * @return 布尔值
	 */
	public static boolean exists(String key){
		return redisTemplate.hasKey(key);
	}

	/**
	 * 发布消息
	 * @param key  频道名称
	 * @param value  消息体
	 */
	public static void publish(String key,String value){
		redisTemplate.convertAndSend(key,value);
	}

	/**
	 * 将消息封装发布到 SKYEYE_WEBSOCKET_CHANNEL
	 * @param clientId 客户端id
	 * @param destination 目的地
	 * @param payload 消息负载
	 */
	public static void publish(String clientId,String destination,Object payload){
		SubscribeMessage message=new SubscribeMessage();
		message.setClientId(clientId);
		message.setDestination(destination);
		message.setPayload(payload);
		redisTemplate.convertAndSend(SKYEYE_WEBSOCKET_CHANNEL, JSONObject.toJSONString(message));
	}
}
