package com.fyf.parkinglot.model;

import com.google.gson.JsonObject;

/**
 * @author fengyifei
 * @category 用于向客户端返回信息的模型
 */
public class ResultInfoBean {

    private int code; // 错误码
    private JsonObject msg; // 信息

    public ResultInfoBean() {

    }

    public ResultInfoBean(int code, JsonObject msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public JsonObject getMsg() {
        return msg;
    }

    public void setMsg(JsonObject msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultInfoBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
