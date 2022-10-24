package com.xdu.formteamtalent.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class RestResult implements Serializable {
    private String code;
    private String msg;
    private Object obj;
    public RestResult(String code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }
    public RestResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static RestResult success() {
        return new RestResult("200", "ok");
    }
    public static RestResult success(Object obj) {
        return new RestResult("200", "ok", obj);
    }
    public static RestResult fail(String code, String msg) {
        return new RestResult(code, msg);
    }
    public static RestResult fail_502() {
        return new RestResult("502", "服务器错误，请重试>_<");
    }
}
