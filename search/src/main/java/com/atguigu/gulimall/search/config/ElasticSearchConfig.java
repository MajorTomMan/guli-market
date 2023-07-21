/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:20:29
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-19 22:59:42
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/config/ElasticSearchConfig.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticSearchConfig {
    @Bean
    public ElasticsearchClient elasticsearchClient(){
        RestClient restClient = restClient();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }
    @Bean
    public RestClient restClient() {
            HttpHost host = new HttpHost("192.168.253.131", 9200, "http");
            RestClientBuilder builder = RestClient.builder(host);
            RestClient restClient = builder.build();
            return restClient;
    }
}
