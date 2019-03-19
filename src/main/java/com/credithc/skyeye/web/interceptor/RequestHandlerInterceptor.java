package com.credithc.skyeye.web.interceptor;

import com.credithc.skyeye.util.DateUtil;
import com.credithc.skyeye.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截器.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/12
 */
public class RequestHandlerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerInterceptor.class);

    /**
     * 在Controller方法前进行拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = LogUtil.createRequestId();
        Thread.currentThread().setName("start-" + requestId + "-" + DateUtil.getCurrentTime(DateUtil.DATE_FORMAT_HMS));//重命名当前线程
//        logger.info("preHandle：" + request.getServletPath() + ":" + handler);
        return true;
    }

    /**
     * 访问的提交
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
            throws Exception {
        if (mv == null) {
            return;
        }
        String path = request.getServletPath();
//        logger.info("postHandle：" + path);
        // 拦截view开关的方法
    }

    /**
     * 在Controller方法后进行拦截
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Thread.currentThread().setName("complete-" + LogUtil.getRequestId() + "-" + DateUtil.getCurrentTime(DateUtil.DATE_FORMAT_HMS));//重命名当前线程
//        logger.info("afterCompletion：" + request.getServletPath() + ":" + handler);
    }

}
