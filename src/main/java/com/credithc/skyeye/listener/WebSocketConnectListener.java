package com.credithc.skyeye.listener;

import com.credithc.skyeye.manager.WebSocketServerManager;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;

/**
 * @Description :websocket创建连接的监听
 * @Author ：dongbin
 * @Date ：2017/9/26
 */
@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {
    @Resource
    private WebSocketServerManager webSocketServerManager;
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        this.webSocketServerManager.processEvent(headerAccessor);
    }
}
