package com.xdu.formteamtalent.global;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EnterInterceptor implements HandlerInterceptor {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("auth");
        Claims claims = JwtUtil.getClaimsByToken(token);
        if (claims == null || JwtUtil.isTokenExpired(claims.getExpiration())) {
            response.sendRedirect("/api/error/" + 4011 + "/" + "token失效");
            return false;
        } else {
            String u_id = claims.getSubject();
            if (userService.getOne(new QueryWrapper<User>().eq("u_id", u_id)) == null) {
                response.sendRedirect("/api/error/" + 4012 + "/" + "token错误");
                return false;
            }
            return true;
        }
    }
}
