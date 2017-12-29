package com.credithc.skyeye.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.credithc.skyeye.manager.WebSocketServerManager;
import com.credithc.skyeye.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author ：dongbin
 * @Description: demo控制器
 * @Date ：2017/6/7
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    /**
     * redis模板类
     */
    @Resource
    private StringRedisTemplate template;
    /**
     * webSocket模板类
     * SimpMessagingTemplate能够在应用的任何地方发送消息，甚至不必以首先接收一条消息作为前提
     */
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    @Resource
    private WebSocketServerManager webSocketServerManager;


    @RequestMapping(value = "/view")
    public String view(){
        logger.info(RedisUtil.get("dongbin"));
        System.out.println(RedisUtil.get("dongbin"));
        return "view";
    }

    /**
     * webSocket dem审核页面
     * @return 展示页
     */
    @RequestMapping(value = "/ws/approve")
    public ModelAndView approveView(){
        ModelAndView modelAndView=new ModelAndView("/demo/webSocketApprove");
        modelAndView.addObject("userId",ThreadLocalRandom.current().nextInt(1000,9999)+"");
//        modelAndView.addObject("userId","kermit");
        return modelAndView;
    }
    /**
     * webSocket demo申请页面
     * @return 展示页
     */
    @RequestMapping(value = "/ws/apply")
    public ModelAndView applyView(){
        ModelAndView modelAndView=new ModelAndView("/demo/webSocketApply");
        modelAndView.addObject("userId",ThreadLocalRandom.current().nextInt(1000,9999)+"");
//        modelAndView.addObject("userId","kermit");
        return modelAndView;
    }

    /**
     *  当浏览器向服务器端发送STOMP请求时，通过@MessageMapping注解来映射/call地址
     *  当服务端有消息时，会对订阅了@SendTo中的路径的客户端发送消息，如果不配置@sendTo，默认会给订阅了“/topic/call”的用户发送消息
     * @param message 消息内容
     * @return 推给订阅者的内容
     */
    @MessageMapping("/ws/call")
    @SendTo("/topic/answer")
    public String answer(String message) {
        JSONObject jsonObject= JSONObject.parseObject(message);
        String retMsg="";
        if(jsonObject!=null){
            retMsg= "welcome,"+jsonObject.getString("name");
        }
        System.out.println(retMsg);
        return retMsg;
    }


    /**
     * 当浏览器向服务器端发送STOMP请求时，通过@MessageMapping注解来映射/call地址
     * @param message  消息内容
     */
    @MessageMapping("/ws/approveCall")
    public void approveCall(String message) {
        JSONObject jsonObject= JSONObject.parseObject(message);
        String retMsg="";
        if(jsonObject!=null){
            String userId=jsonObject.getString("userId");
            retMsg= "welcome,"+jsonObject.getString("name");
            RedisUtil.publish(userId,"/approverChannel",retMsg);
//            simpMessagingTemplate.convertAndSend("/queue/receiveVideo",retMsg);
            System.out.println(webSocketServerManager.getAcceptorClientMap());
        }
    }
    /**
     * 当浏览器向服务器端发送STOMP请求时，通过@MessageMapping注解来映射/call地址
     * @param message  消息内容
     */
    @MessageMapping("/ws/applyCall")
    public void applyCall(String message) {
        JSONObject jsonObject= JSONObject.parseObject(message);
        String retMsg="";
        if(jsonObject!=null){
            String userId=jsonObject.getString("userId");
            retMsg= "welcome,"+jsonObject.getString("name");
            RedisUtil.publish(userId,"/applicantChannel",retMsg);
            System.out.println(webSocketServerManager.getApplyClientMap());
        }
    }
}
