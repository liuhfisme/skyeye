package com.credithc.skyeye.bean;

import java.io.Serializable;

/**
 * @Description : 订阅消息bean
 * @Author ：dongbin
 * @Date ：2017/9/19
 */
public class SubscribeMessage implements Serializable {
    private static final long serialVersionUID = -6656321584333688695L;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
