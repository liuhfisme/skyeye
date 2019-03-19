package com.credithc.skyeye.bean;

import lombok.Data;

/**
 * 订阅消息bean.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/9/19
 */
@Data
public class SubscribeMessage {
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 发送的目的地
     */
    private String destination;
    /**
     * 消息负载
     */
    private Object payload;

}
