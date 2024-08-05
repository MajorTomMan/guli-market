package com.atguigu.gulimall.cart.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.atguigu.gulimall.cart.interceptor.CartInterceptor;

public class WebConfig  implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
