/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-18 23:38:15
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-18 23:56:36
 * @FilePath: \Guli\product\src\main\java\com\atguigu\gulimall\product\config\ProductThreadConfig.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.config;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atguigu.gulimall.product.properties.ThreadPoolConfigProperties;

@Configuration
public class ProductThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(
            pool.getCoreSize(), pool.getMaxSize(), pool.getKeepAliveTime(),
            TimeUnit.SECONDS, new LinkedBlockingDeque<>(100000),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
