package com.xdu.formteamtalent.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestfulResponse implements Serializable {
    private Integer code;
    private String msg;
    private Object obj;

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
    public static RestfulResponse fail_500() {
        return new RestfulResponse(500, "服务器错误，请重试>_<");
    }
}
