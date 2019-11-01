package com.credithc.skyeye.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * websocket连接完成的监听(这是一个connect_ack事件).
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/9/26
 */
//@Component
public class WebSocketConnectedListener implements ApplicationListener<SessionConnectedEvent> {
    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
    }
}
