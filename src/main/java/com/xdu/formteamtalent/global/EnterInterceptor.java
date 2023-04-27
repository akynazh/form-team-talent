package com.xdu.formteamtalent.global;

import com.xdu.formteamtalent.utils.AuthUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Component
public class EnterInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (AuthUtil.checkToken(request)) {
            return true;
        } else {
            response.sendRedirect("/api/error/401/" + URLEncoder.encode("token验证失败", "UTF-8"));
            return false;
        }
    }
}
