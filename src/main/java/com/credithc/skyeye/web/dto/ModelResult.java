package com.credithc.skyeye.web.dto;

import lombok.Data;

/**
 * 响应结果.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/9
 */
@Data
public class ModelResult {
    /**
     * 响应码
     */
    private Integer rspCode;
    /**
     * 响应信息
     */
    private String rspMsg;
    /**
     * 响应体对象
     */
    private Object rspObj;

}
