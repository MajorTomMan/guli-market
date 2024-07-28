/*
 * @Date: 2023-10-23 00:33:20
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-07-28 23:06:57
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\config\WebViewConfig.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebViewConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // TODO Auto-generated method stub
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/register.html").setViewName("register");
    }
    
}
