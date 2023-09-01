/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-06-23 17:37:38
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-01 21:36:17
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\SearchApplication.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.atguigu.gulimall.search.feign")
@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class })
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}
