/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:20:29
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-30 13:25:46
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/config/ElasticSearchConfig.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    public RestClient restClient(){
        HttpHost host = new HttpHost("localhost",9200,"http");
        RestClientBuilder builder=RestClient.builder(host);
        return builder.build();
    }
}
