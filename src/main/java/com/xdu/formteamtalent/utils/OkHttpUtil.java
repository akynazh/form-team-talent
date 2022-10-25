package com.xdu.formteamtalent.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class OkHttpUtil {
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     *
     * @param url 请求地址
     * @param params 请求参数
     * @return 返回json对象
     */
    public static JSONObject get(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        boolean firstFlag = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println(entry.getValue());
            if (firstFlag) {
                sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                firstFlag = false;
            } else {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        Request req = new Request.Builder().url(sb.toString()).build();
        return sendRequest(req);
    }

    public static JSONObject get(String url) {
        Request req = new Request.Builder().url(url).build();
        return sendRequest(req);
    }

    public static JSONObject post(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request req = new Request.Builder().url(url).post(requestBody).build();
        return sendRequest(req);
    }

    private static JSONObject sendRequest(Request req) {
        try (Response resp = okHttpClient.newCall(req).execute()) {
            if (resp.isSuccessful()) {
                assert resp.body() != null;
                return JSONUtil.parseObj(resp.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
