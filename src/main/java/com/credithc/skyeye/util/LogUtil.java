package com.credithc.skyeye.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * log工具类.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/11/7
 */
public class LogUtil {
    /**
     * 创建请求id
     * @return 请求id
     */
    public static String createRequestId(){
        String requestId = UUID.randomUUID().toString().replace("-", "").substring(16);
        MDC.put("requestId", requestId);
        return requestId;
    }

    /**
     * 获取一个请求id，如果没有则创建一个
     * @return 请求id
     */
    public static String getOneRequestId(){
        String requestId=MDC.get("requestId");
        if(StringUtils.isBlank(requestId)){
            requestId = UUID.randomUUID().toString().replace("-", "").substring(16);
        }
        MDC.put("requestId", requestId);
        return requestId;
    }

    /**
     * 从当前线程中获取请求id
     * @return 请求id
     */
    public static String getRequestId(){
        return  MDC.get("requestId");
    }
}
