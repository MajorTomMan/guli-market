/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-23 21:13:06
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-10 22:55:58
 * @FilePath: /common/home/master/project/GuliMall/gateway/src/main/java/com/atguigu/gulimall/gateway/GatewayApplication.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.gateway;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class })
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
