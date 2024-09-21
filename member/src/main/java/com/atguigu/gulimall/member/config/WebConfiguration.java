package com.atguigu.gulimall.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.atguigu.gulimall.member.interceptor.LoginUserInterceptor;

/**
 * WebConfiguration
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginUserInterceptor()).addPathPatterns("/**");
    }
}