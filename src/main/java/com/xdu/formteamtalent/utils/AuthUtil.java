package com.xdu.formteamtalent.utils;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthUtil {
    private static String getOpenIdUrl;
    private static String appId;
    private static String appSecret;


    @Value("${wx.appId}")
    public void setAppId(String appId) {
        AuthUtil.appId = appId;
    }
    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        AuthUtil.appSecret = appSecret;
    }

    @Value("${wx.getOpenIdUrl}")
    public void setGetOpenIdUrl(String getOpenIdUrl) {
        AuthUtil.getOpenIdUrl = getOpenIdUrl;
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

    public static String getUserId(HttpServletRequest request) {
        String token = request.getHeader("auth");
        Claims claims = JwtUtil.getClaimsByToken(token);
        assert claims != null;
        return claims.getSubject();
    }

    public static Boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("auth");
        if (token == null) { // token不存在
            return false;
        }
        Claims claims = JwtUtil.getClaimsByToken(token);
        if (claims == null || JwtUtil.isTokenExpired(claims.getExpiration())) { // token错误或过期
            return false;
        }
        return true;
    }
}
