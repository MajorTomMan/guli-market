/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:20:29
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-31 23:36:56
 * @FilePath: /common/home/master/project/GuliMall/search/src/main/java/com/atguigu/gulimall/search/config/ElasticSearchConfig.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    public RestClient restClient() {
        HttpHost host = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(host);
        /* 设置头部 */
        Header[] defaultHeader = new Header[] { new BasicHeader("Content-Type", "application/json") };
        builder.setDefaultHeaders(defaultHeader);
        /* 设置错误监听函数 */
        builder.setFailureListener(
                new FailureListener() {
                    @Override
                    public void onFailure(Node node) {
                        System.out.println(node);
                    }
                });
        /* 设置默认请求配置回调 */
        builder.setRequestConfigCallback(
                new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setSocketTimeout(10000);
                    }
                });
        return builder.build();
    }
}
