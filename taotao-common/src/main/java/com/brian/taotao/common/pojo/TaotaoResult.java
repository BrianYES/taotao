package com.brian.taotao.common.pojo;

import java.io.Serializable;

public class TaotaoResult implements Serializable {

    private Integer status;
    private String msg;
    private Object data;

    public TaotaoResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static TaotaoResult build(Integer status, String msg, Object data) {
        return new TaotaoResult(status, msg, data);
    }

    public static TaotaoResult build(Integer status, String msg) {
        return new TaotaoResult(status, msg, null);
    }

    public static TaotaoResult ok() {
        return build(200, "OK");
    }

    public static TaotaoResult ok(Object data) {
        return build(200, "OK", data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
