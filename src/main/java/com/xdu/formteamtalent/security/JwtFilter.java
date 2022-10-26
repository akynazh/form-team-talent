package com.xdu.formteamtalent.security;

import cn.hutool.json.JSONUtil;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {
    /**
     * 拦截用户请求
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("auth");
        // 如果没有token，则直接通过，后续通过@RequiresAuthentication等进行拦截
        if (StringUtils.isEmpty(token)) {
            return true;
        } else { // 如果有token，则进行校验
            // 校验token
            Claims claims = JwtUtil.getClaimsByToken(token);
            if (claims == null || JwtUtil.isTokenExpired(claims.getExpiration())) {
                throw new ExpiredCredentialsException("token已经失效，请重新登录");
            }
            // 继续继续校验，校验成功则登录成功
            return executeLogin(servletRequest, servletResponse);
        }
    }

    /**
     * 把请求头中的token生成到JwtToken类中
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("auth");
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return new JwtToken(token);
    }

    /**
     * 登录失败返回错误消息
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        RestfulResponse restfulResponse = RestfulResponse.fail(400, throwable.getMessage());
        String JsonStr = JSONUtil.toJsonStr(restfulResponse);
        try {
            httpServletResponse.getWriter().print(JsonStr);
        } catch (IOException ignored){}
        return false;
    }

    /**
     * 用于支持跨域
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        /*
        假如：浏览器地址栏的地址是：http://localhost:4200/#/pages/dashadmin
        异步请求后端地址：http://localhost:8080/secure
        请求头：
        ...
        Host:localhost:8080
        Origin:http://localhost:4200
        Referer:http://localhost:4200/
        ...
        从上面的请求头中可以看到request.getHeader("Origin")的值就是就是http://localhost:4200
        request.getHeader("Origin")经常用法就是用来解决浏览器跨域问题
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        指的是只允许http://localhost:4200跨域访问后端服务器。
        */
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");

        /*
        响应首部 Access-Control-Allow-Headers 用于 preflight request （预检请求）中，
        列出了将会在正式请求的 Access-Control-Expose-Headers 字段中出现的首部信息。修改为请求首部
         */
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        /*
        options 请求就是预检请求，可用于检测服务器允许的http 方法。
        当发起跨域请求时，由于安全原因，触发一定条件时浏览器会在正式请求之前自动先发起OPTIONS 请求，即CORS 预检请求，
        服务器若接受该跨域请求，浏览器才继续发起正式请求。
         */
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
