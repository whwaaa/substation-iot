package com.iot.utils;

public class AjaxResult {

    private Integer code;
    private String msg;
    private Object obj;

    public AjaxResult(Object obj) {
        this.code = 200;
        this.msg = "ok";
        this.obj = obj;
    }

    public AjaxResult() {
        this.code = 200;
        this.msg = "ok";
    }

    public AjaxResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
