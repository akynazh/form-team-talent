package com.xdu.formteamtalent.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WxUtil {
    private final String getOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session";
    @Value("wx.appId")
    private String appId;
    @Value("wx.appSecret")
    private String appSecret;

    public String getOpenId(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        return OkHttpUtil.get(getOpenIdUrl, params);
    }
}
