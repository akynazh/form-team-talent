package com.xdu.formteamtalent.controller;

import com.xdu.formteamtalent.entity.RestfulResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/api")
@ApiIgnore
public class CommonController {
    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  消息
     * @return RestfulResponse
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/error/{code}/{msg}")
    public RestfulResponse error(@PathVariable Integer code,
                                 @PathVariable String msg) throws UnsupportedEncodingException {
        return RestfulResponse.fail(code, URLDecoder.decode(msg, "UTF-8"));
    }
}