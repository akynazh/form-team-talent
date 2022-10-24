package com.xdu.formteamtalent.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class OkHttpUtil {
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     *
     * @param url 请求地址
     * @param parms 请求参数
     * @return
     */
    public static String get(String url, Map<String, String> parms) {
        StringBuilder sb = new StringBuilder(url);
        boolean firstFlag = true;
        for (Map.Entry<String, String> entry : parms.entrySet()) {
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

    public static String post(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request req = new Request.Builder().url(url).post(requestBody).build();
        return sendRequest(req);
    }

    private static String sendRequest(Request req) {
        try (Response resp = okHttpClient.newCall(req).execute()) {
            if (resp.isSuccessful()) {
                assert resp.body() != null;
                return resp.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
