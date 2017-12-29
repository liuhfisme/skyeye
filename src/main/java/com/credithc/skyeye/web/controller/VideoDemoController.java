package com.credithc.skyeye.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description :视频demo
 * @Author ：dongbin
 * @Date ：2017/10/31
 */
@Controller
@RequestMapping("/video")
public class VideoDemoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoDemoController.class);
    /**
     * webSocket dem审核页面
     * @return 展示页
     */
    @RequestMapping(value = "/approve")
    public ModelAndView approveView(){
        logger.info("video....approve");
        ModelAndView modelAndView=new ModelAndView("/video/approve");
        //modelAndView.addObject("userId", ThreadLocalRandom.current().nextInt(1000,9999)+"");
        modelAndView.addObject("userId", "111111");
        return modelAndView;
    }
    /**
     * webSocket demo申请页面
     * @return 展示页
     */
    @RequestMapping(value = "/apply")
    public ModelAndView applyView(){
        ModelAndView modelAndView=new ModelAndView("/video/apply");
        modelAndView.addObject("userId",ThreadLocalRandom.current().nextInt(1000,9999)+"");
        return modelAndView;
    }

}
