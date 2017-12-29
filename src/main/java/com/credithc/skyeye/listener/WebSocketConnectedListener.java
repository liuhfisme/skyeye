package com.credithc.skyeye.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;


/**
 * @Description :websocket连接完成的监听(这是一个connect_ack事件)
 * @Author ：dongbin
 * @Date ：2017/9/26
 */
//@Component
public class WebSocketConnectedListener implements ApplicationListener<SessionConnectedEvent> {
    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
    }
}
