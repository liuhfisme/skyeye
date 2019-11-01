package com.credithc.skyeye.listener;

import com.credithc.skyeye.manager.WebSocketServerManager;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;

/**
 * websocket创建连接的监听.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/9/26
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
