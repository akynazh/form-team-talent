package com.xdu.formteamtalent.controller;

import com.xdu.formteamtalent.global.RestfulResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/api")
public class CommonController {
    @RequestMapping("/error/{code}/{msg}")
    public RestfulResponse error(@PathVariable Integer code, @PathVariable String msg) throws UnsupportedEncodingException {
        return RestfulResponse.fail(code, URLDecoder.decode(msg, "UTF-8"));
    }
}