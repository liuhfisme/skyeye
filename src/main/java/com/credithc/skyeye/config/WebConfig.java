package com.credithc.skyeye.config;

import com.credithc.skyeye.web.interceptor.RequestHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description : web配置类
 * @Author ：dongbin
 * @Date ：2017/6/12
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * 定义拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHandlerInterceptor());
    }

}