package com.xdu.formteamtalent.config;

import com.xdu.formteamtalent.global.EnterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private EnterInterceptor enterInterceptor;

    @Autowired
    public void setEnterInterceptor(EnterInterceptor enterInterceptor) {
        this.enterInterceptor = enterInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(enterInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/auth")
                .excludePathPatterns("/api/user/authwx")
                .excludePathPatterns("/api/activity/get/pub")
                .excludePathPatterns("/api/activity/search/**")
                .excludePathPatterns("/api/error/**")
                // 二维码访问
                .excludePathPatterns("/qrcode/**")
                // 错误界面
                .excludePathPatterns("/error")
                // swagger 文档
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/doc.html",
                        "/swagger-resources/**",
                        "/v3/**");
    }
}
