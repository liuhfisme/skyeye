package com.credithc.skyeye.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * RabbitMQ.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/27
 */
@RestController
@RequestMapping("/mq")
public class RabbitMQController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    private int count;

    @RequestMapping(method = RequestMethod.POST)
    public Object addQueue() {
        System.out.println("addQueue....");
//        Object obj=rabbitTemplate.convertSendAndReceive("myexchange_1","routing_key_1","测试数据");
//        for (int i = 0; i < 100; i++) {
//            count++;
//            this.rabbitTemplate.convertAndSend("myexchange_1", "routing_key_1", "测试数据" + count);
//        }
        File file = new File("E:\\jsdintopiece.txt");
        try {
            List<String> list = FileUtils.readLines(file, "UTF-8");
            for (String str : list) {
                System.out.println(str);
                this.rabbitTemplate.convertAndSend("myexchange_1", "routing_key_1", str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        this.rabbitTemplate.convertAndSend("myexchange_other","routing_key_other","测试数据other"+count);
        return "addQueue....";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getQueue() {
        System.out.println("getQueue....");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queue1", rabbitTemplate.receiveAndConvert("queue1"));
//        jsonObject.put("queue2", rabbitTemplate.receiveAndConvert("queue2"));
//        jsonObject.put("queue3", rabbitTemplate.receiveAndConvert("queue3"));
        return jsonObject.toJSONString();
    }
}
