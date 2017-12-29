package com.credithc.skyeye.listener;

import com.credithc.skyeye.manager.WebSocketServerManager;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import javax.annotation.Resource;

/**
 * @Description : websocket断开连接的监听
 * @Author ：dongbin
 * @Date ：2017/9/26
 */
@Component
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent>{
    @Resource
    private WebSocketServerManager webSocketServerManager;
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
//        System.out.println("SessionDisconnectListener:"+sessionDisconnectEvent.getSource());
//        System.out.println("SessionDisconnectListener:"+sessionDisconnectEvent.getCloseStatus());
//        System.out.println("SessionDisconnectListener:"+sessionDisconnectEvent.getSessionId());
//        System.out.println("SessionDisconnectListener:"+sessionDisconnectEvent.toString());
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        this.webSocketServerManager.processEvent(headerAccessor);
    }
}
