/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-21 11:37:45
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-11 22:58:57
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/config/RedissonConfig.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.253.131:6379").setConnectTimeout(30000).setPassword("root");
        return Redisson.create(config);
    }
}
