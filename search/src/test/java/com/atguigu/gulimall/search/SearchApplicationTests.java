/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-30 16:19:56
 * @FilePath: /common/home/master/project/GuliMall/search/src/test/java/com/atguigu/gulimall/search/SearchApplicationTests.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search;

import java.io.IOException;

import com.atguigu.gulimall.search.enitty.TestEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestClient restClient;
    @Test
    public void testAdd() throws IOException{
        String index="tttttest";
        String type="_doc";
        String id="1";
        String path="/"+index+"/"+type+"/"+id;
        TestEntity test = new TestEntity("gg", "man");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(test);
        HttpEntity entity=new NStringEntity(json,ContentType.APPLICATION_JSON);
        Request request=new Request("post",path);
        request.setEntity(entity);
        restClient.performRequest(request);
    }
}
