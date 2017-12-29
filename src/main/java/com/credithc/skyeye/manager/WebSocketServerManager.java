package com.credithc.skyeye.manager;

import com.credithc.skyeye.util.LogUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description : webSocket服务端管理
 * @Author ：dongbin
 * @Date ：2017/9/19
 */
@Component
public class WebSocketServerManager {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerManager.class);
    /**
     * 服务端节点
     */
    public static final String APPLY_ENDPOINT = "applicantEndpoint";//视频申请方的连接节点
    public static final String ACCEPT_ENDPOINT = "acceptorEndpoint";//视频接受方的连接节点，创建会议房间
    private static AtomicInteger applicantConnectCount = new AtomicInteger(0);//视频申请方的连接数
    private static AtomicInteger acceptorConnectCount = new AtomicInteger(0);//视频接受方的连接数

    /**
     * 属性值
     */
    private final String SESSION_ID = "sessionId";
    private final String ENDPOINT_NAME = "endpointName";
    private final String CLIENT_ID = "clientId";
    /**
     * 客户端列表，key：客户端id  value:sessionId集合
     */
    private final Map<String, Set<String>> applicantClientMap = Maps.newConcurrentMap();
    private final Map<String, Set<String>> acceptorClientMap = Maps.newConcurrentMap();

    @Resource
    private SubProtocolWebSocketHandler subProtocolWebSocketHandler;

    /**
     * 连接管道处理
     *
     * @param stompHeaderAccessor stomp头消息访问器
     */
    public void processChannel(StompHeaderAccessor stompHeaderAccessor) {
    }

    /**
     * 处理 websocket的相关事件
     *
     * @param stompHeaderAccessor stomp头消息访问器
     */
    public void processEvent(StompHeaderAccessor stompHeaderAccessor) {
//        logger.debug("--------------------------------------------");
//        System.out.println("Command:" + stompHeaderAccessor.getCommand());
//        System.out.println("SessionId:" + stompHeaderAccessor.getSessionId());
//        System.out.println("SubscriptionId:" + stompHeaderAccessor.getSubscriptionId());
//        System.out.println("Heartbeat:"+stompHeaderAccessor.getHeartbeat());
//        System.out.println("SessionAttributes:" + stompHeaderAccessor.getSessionAttributes());
//        String endPoint = (String) stompHeaderAccessor.getSessionAttributes().get(ENDPOINT_NAME);
//        System.out.println("endPoint:" + endPoint);
//        System.out.println(CLIENT_ID + ":" + stompHeaderAccessor.getFirstNativeHeader(CLIENT_ID));
        LogUtil.getOneRequestId();
        switch (stompHeaderAccessor.getCommand()) {
            case CONNECT:
                //自定义的头信息通过getFirstNativeHeader来获取。
                stompHeaderAccessor.getSessionAttributes().put(CLIENT_ID, stompHeaderAccessor.getFirstNativeHeader(CLIENT_ID));
                this.addClient(stompHeaderAccessor);
                break;
            case DISCONNECT:
                this.removeClient(stompHeaderAccessor);
                break;
            default:
                break;
        }
    }

    /**
     * 握手处理
     *
     * @param request    http请求对象
     * @param attributes stomp的session属性
     */
    public void processHandshake(ServerHttpRequest request, Map<String, Object> attributes) {
        String path = request.getURI().getPath();
        //标识出连接的是哪个服务端点
        if (path.indexOf(APPLY_ENDPOINT) > 0) {
            attributes.put(ENDPOINT_NAME, APPLY_ENDPOINT);
        } else if (path.indexOf(ACCEPT_ENDPOINT) > 0) {
            attributes.put(ENDPOINT_NAME, ACCEPT_ENDPOINT);
        }
    }

    /**
     * 添加客户端
     *
     * @param stompHeaderAccessor stomp头信息访问器
     */
    private void addClient(StompHeaderAccessor stompHeaderAccessor) {
        String endPoint = (String) stompHeaderAccessor.getSessionAttributes().get(ENDPOINT_NAME);
        String clientId = (String) stompHeaderAccessor.getSessionAttributes().get(CLIENT_ID);
        String sessionId = stompHeaderAccessor.getSessionId();
        if (StringUtils.isAnyBlank(endPoint, clientId, sessionId)) {
            logger.info(endPoint + "_" + clientId + "_" + sessionId + ":不能有任何一值为空");
            return;
        }
        Map<String, Set<String>> tempMap;
        switch (endPoint) {
            case APPLY_ENDPOINT:
                tempMap = applicantClientMap;
                break;
            case ACCEPT_ENDPOINT:
                tempMap = acceptorClientMap;
                break;
            default:
                tempMap = null;
                break;
        }
        if (tempMap == null) {
            return;
        }
        if (tempMap.containsKey(clientId)) {
            tempMap.get(clientId).add(sessionId);
        } else {
            Set<String> sessionIdSet = Sets.newHashSet(sessionId);
            tempMap.put(clientId, sessionIdSet);
        }
        this.incrConnectCount(endPoint);

        logger.info(endPoint + "_" + clientId + "_" + sessionId + ":" + "该用户新建立了一个连接，用户连接数为" + tempMap.get(clientId).size() + ",当前总连接数为" + this.getConnectCount(endPoint));
    }

    /**
     * 删除客户端
     *
     * @param stompHeaderAccessor stomp头信息访问器
     */
    private void removeClient(StompHeaderAccessor stompHeaderAccessor) {
        String endPoint = (String) stompHeaderAccessor.getSessionAttributes().get(ENDPOINT_NAME);
        String clientId = (String) stompHeaderAccessor.getSessionAttributes().get(CLIENT_ID);
        String sessionId = stompHeaderAccessor.getSessionId();
        Map<String, Set<String>> tempMap;
        switch (endPoint) {
            case APPLY_ENDPOINT:
                tempMap = applicantClientMap;
                break;
            case ACCEPT_ENDPOINT:
                tempMap = acceptorClientMap;
                break;
            default:
                tempMap = null;
                break;
        }
        if (tempMap != null && tempMap.containsKey(clientId)) {
            Set<String> sessionIdSet = tempMap.get(clientId);
            int userConnectCount = 0;
            if (sessionIdSet.contains(sessionId)) {
                sessionIdSet.remove(sessionId);
                this.decrConnectCount(endPoint);
                if (sessionIdSet.isEmpty()) {
                    tempMap.remove(clientId);
                } else {
                    userConnectCount = sessionIdSet.size();
                }
            }
            logger.info(endPoint + "_" + clientId + "_" + sessionId + ":" + "该用户断开了一个连接，用户连接数为" + userConnectCount + ",当前总连接数为" + this.getConnectCount(endPoint));
        }
    }

    /**
     * 取端点的连接数
     *
     * @param endPointName 端点名称
     * @return 连接数
     */
    private int getConnectCount(String endPointName) {
        Integer count;
        switch (endPointName) {
            case APPLY_ENDPOINT:
                count = applicantConnectCount.intValue();
                break;
            case ACCEPT_ENDPOINT:
                count = acceptorConnectCount.intValue();
                break;
            default:
                count = null;
                break;
        }
        return count;
    }

    /**
     * 连接数加一
     *
     * @param endPointName 端点名称
     */
    private void incrConnectCount(String endPointName) {
        switch (endPointName) {
            case APPLY_ENDPOINT:
                applicantConnectCount.incrementAndGet();
                break;
            case ACCEPT_ENDPOINT:
                acceptorConnectCount.incrementAndGet();
                break;
            default:
                break;
        }
    }

    /**
     * 连接数减一
     *
     * @param endPointName 端点名称
     */
    private void decrConnectCount(String endPointName) {
        switch (endPointName) {
            case APPLY_ENDPOINT:
                applicantConnectCount.decrementAndGet();
                break;
            case ACCEPT_ENDPOINT:
                acceptorConnectCount.decrementAndGet();
                break;
            default:
                break;
        }
    }

    /**
     * 关闭所有的seesion，调用这个方法关闭session会导致客户端连接异常，进行重连
     */
    public void closeAllSession() {
        this.subProtocolWebSocketHandler.stop();
        this.subProtocolWebSocketHandler.start();
    }

    /**
     * 清空视频接受端的所有连接数
     */
    public void clearAcceptorConns() {
        this.acceptorClientMap.clear();
        this.acceptorConnectCount = new AtomicInteger(0);
        logger.info("释放的ws Acceptor 连接数为：" + this.acceptorConnectCount.intValue());
    }

    public Map<String, Set<String>> getApplyClientMap() {
        return applicantClientMap;
    }

    public Map<String, Set<String>> getAcceptorClientMap() {
        return acceptorClientMap;
    }
}
