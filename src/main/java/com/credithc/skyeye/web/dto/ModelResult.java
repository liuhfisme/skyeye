package com.credithc.skyeye.web.dto;

/**
 * @Description: 响应结果
 * @Author ：dongbin
 * @Date ：2017/6/9
 */
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

    public Integer getRspCode() {
        return rspCode;
    }

    public void setRspCode(Integer rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public Object getRspObj() {
        return rspObj;
    }

    public void setRspObj(Object rspObj) {
        this.rspObj = rspObj;
    }
}
