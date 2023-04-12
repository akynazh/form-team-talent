package com.xdu.formteamtalent.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("通用返回数据")
public class RestfulResponse implements Serializable {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("返回消息")
    private String msg;
    @ApiModelProperty("数据对象")
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
