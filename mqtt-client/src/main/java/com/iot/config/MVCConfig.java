package com.iot.config;

import com.iot.interceptor.GlobalInterceptor;
import com.iot.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        GlobalInterceptor interceptor = new GlobalInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/requestreset","/login","/css/**","/fonts/**","/img/**"
                        ,"/js/**","/Linearicons-Free-v1.0.0/**","/mqtt/**","/*.html");
    }
}
