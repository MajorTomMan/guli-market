/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-30 18:14:37
 * @FilePath: /common/home/master/project/GuliMall/search/src/main/java/com/atguigu/gulimall/search/SearchApplication.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}
