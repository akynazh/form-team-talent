package com.xdu.formteamtalent.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class RestfulResponse implements Serializable {
    private Integer code;
    private String msg;
    private Object obj;
    public RestfulResponse(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }
    public RestfulResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static RestfulResponse success() {
        return new RestfulResponse(200, "ok");
    }
    public static RestfulResponse success(Object obj) {
        return new RestfulResponse(200, "ok", obj);
    }
    public static RestfulResponse fail(Integer code, String msg) {
        return new RestfulResponse(code, msg);
    }
    public static RestfulResponse fail_502() {
        return new RestfulResponse(502, "服务器错误，请重试>_<");
    }
}
