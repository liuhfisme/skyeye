package com.credithc.skyeye.config;

import com.credithc.skyeye.manager.WebSocketServerManager;
import com.credithc.skyeye.util.DateUtil;
import com.credithc.skyeye.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import javax.annotation.Resource;
import java.util.Map;

/**
 * WebSocket配置.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/16
 */
/*
 * EnableWebSocketMessageBroker代表不仅启用websocket而且启用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    @Resource
    private WebSocketServerManager webSocketServerManager;
    /**
     * 服务端心跳检测[ping,pong],单位ms
     */
    private final long[] SERVER_HEARTBEAT={2000,3000};
    /**
     * 允许跨域连接的地址
     */
    private final String[] ALLOWED_ORIGINS={"*"};

    /**
     * 注册STOMP协议的端点，并指定映射的URL，客户端在订阅或发布消息到目的地路径前，要连接该端点
     *
     * @param stompEndpointRegistry stomp端点注册器
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //注册一个endpoint,并指定 SockJS协议。   点对点-用
       // stompEndpointRegistry.addEndpoint("/"+WebSocketServerManager.APPLY_ENDPOINT, "/"+WebSocketServerManager.ACCEPT_ENDPOINT).addInterceptors(new CustomHandshakeInterceptor()).setAllowedOrigins(ALLOWED_ORIGINS);
        stompEndpointRegistry.addEndpoint( "/"+WebSocketServerManager.ACCEPT_ENDPOINT,"/"+WebSocketServerManager.APPLY_ENDPOINT).addInterceptors(new CustomHandshakeInterceptor()).setAllowedOrigins(ALLOWED_ORIGINS);
    }
    /**
     * 配置消息代理
     *
     * @param registry 消息代理注册
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");//从页面上发送的消息，需要带app前缀
        ThreadPoolTaskScheduler taskScheduler=new ThreadPoolTaskScheduler();//初始化任务调度线程，才能使用心跳检测
        taskScheduler.initialize();
        registry.setCacheLimit(2);
        registry.enableSimpleBroker("/user", "/topic","/queue").setHeartbeatValue(SERVER_HEARTBEAT).setTaskScheduler(taskScheduler);//消息代理会处理前缀为/topic的消息
    }

    /**
     * 输入通道参数设置(输入通道传输客户端发给服务端的信息)
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new CustomChannelInterceptor());
    }

    /**
     * 输出通道参数设置(输出通道传输服务端发给客户端的信息)
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new CustomChannelInterceptor());
    }


    /**
     * webSocket握手拦截器（handshake）接口
     */
    private class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

        CustomHandshakeInterceptor() {}

        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response, WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {
            //检查session的值是否存在
            LogUtil.createRequestId();
            String path = request.getURI().getPath();
            System.out.println("beforeHandshake:" + path);
            webSocketServerManager.processHandshake(request,attributes);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }


        @Override
        public void afterHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Exception ex) {
            super.afterHandshake(request, response, wsHandler, ex);
        }
    }


    /**
     * 自定义渠道拦截
     */
    private class CustomChannelInterceptor extends ChannelInterceptorAdapter {
        CustomChannelInterceptor() {}

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            String requestId= LogUtil.createRequestId();
            Thread.currentThread().setName("start-" + requestId + "-" + DateUtil.getCurrentTime(DateUtil.DATE_FORMAT_HMS));//重命名当前线程
            logger.info("preSend");
            return message;
        }
        public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
            logger.info("postSend");
        }
        public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
            Thread.currentThread().setName("complete-" + LogUtil.getRequestId() + "-" + DateUtil.getCurrentTime(DateUtil.DATE_FORMAT_HMS));//重命名当前线程
            logger.info("afterSendCompletion");
        }
    }
}
