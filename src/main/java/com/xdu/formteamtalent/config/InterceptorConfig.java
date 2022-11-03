package com.xdu.formteamtalent.config;

import com.xdu.formteamtalent.global.EnterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
                // /api/user
                .excludePathPatterns("/api/user/auth")

                // /api/activity
                .excludePathPatterns("/api/activity/get/**")
                .addPathPatterns("/api/activity/get/my")

                // api/group

                // other
                .excludePathPatterns("/api/error/**")
                .excludePathPatterns("/qrcode/**");
    }
}
