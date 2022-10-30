package com.xdu.formteamtalent.controller;

import com.xdu.formteamtalent.global.RestfulResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommonController {
    @RequestMapping("/error/{code}/{msg}")
    public RestfulResponse error(@PathVariable Integer code, @PathVariable String msg) {
        return RestfulResponse.fail(code, msg);
    }
}
