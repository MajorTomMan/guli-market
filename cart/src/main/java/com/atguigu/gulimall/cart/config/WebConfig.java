/*
 * @Date: 2024-08-06 14:08:43
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-13 00:40:17
 * @FilePath: \Guli\cart\src\main\java\com\atguigu\gulimall\cart\config\WebConfig.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.atguigu.gulimall.cart.interceptor.CartInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
