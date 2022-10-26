package com.xdu.formteamtalent.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xdu.formteamtalent.security.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WxUtil {
    private static String getOpenIdUrl;
    private static String appId;
    private static String appSecret;

    @Value("${wx.appId}")
    public void setAppId(String appId) {
        WxUtil.appId = appId;
    }
    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        WxUtil.appSecret = appSecret;
    }

    @Value("${wx.getOpenIdUrl}")
    public void setGetOpenIdUrl(String getOpenIdUrl) {
        WxUtil.getOpenIdUrl = getOpenIdUrl;
    }

    /**
     * 获取openId
     * @param code wx.login()之后获取得到的code
     * @return openid
     */
    public static String getOpenIdByCode(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        JSONObject jsonObject = OkHttpUtil.get(getOpenIdUrl, params);
        if (jsonObject != null) {
            return (String) jsonObject.get("openid");
        } else {
            return null;
        }
    }

    /**
     * 通过认证后，获取openid
     */
    public static String getOpenId() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        AccountProfile profile = JSONUtil.parse(principal).toBean(AccountProfile.class);
        return profile.getU_open_id();
    }
}
