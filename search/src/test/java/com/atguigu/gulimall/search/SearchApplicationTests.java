/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-03 12:18:20
 * @FilePath: /common/home/master/project/GuliMall/search/src/test/java/com/atguigu/gulimall/search/SearchApplicationTests.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.atguigu.gulimall.search.enitty.TestEntity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.transport.ElasticsearchTransport;
import lombok.extern.slf4j.Slf4j;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;


@Slf4j
@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestClient restClient;

    private ElasticsearchClient init(){
         // Create the transport with a Jackson mapper
         ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
         ElasticsearchClient esClient = new ElasticsearchClient(transport);
         return esClient;
    }

    @Test
    public void insertData() throws IOException {
        /*
         * String index="tttttest";
         * String type="_doc";
         * String id="1";
         * String path=createPath(index, type, id);
         * Map<String,List<TestEntity>> data=new HashMap<>();
         * List<TestEntity> list=new ArrayList<>();
         * list.add(new TestEntity("gg", "gg"));
         * data.put("doc", list);
         * SendData("post",path,data);
         */
        ElasticsearchClient esClient = init();
        TestEntity testEntity = new TestEntity("gg", "gg");
        IndexResponse response = esClient.index(
                i -> i.index("testentity")
                        .id("1").document(testEntity));
        log.info("Indexed with version " + response.version());
    }

    @Test
    public void deleteData() throws IOException {
        String index = "tttttest";
        String type = "_doc";
        String id = "1";
        String path = createPath(index, type, id);
        SendData("delete", path, Collections.emptyMap());
    }

    @Test
    public void queryData() throws IOException {
        String index = "tttttest";
        String type = "_doc";
        String id = "1";
        String path = createPath(index, type, id);
        SendData("get", path, Collections.emptyMap());
    }

    @Test
    public void updateData() throws IOException {
        String index = "tttttest";
        String type = "_update";
        String id = "1";
        String path = createPath(index, type, id);
        Map<String, List<TestEntity>> data = new HashMap<>();
        List<TestEntity> list = new ArrayList<>();
        list.add(new TestEntity("gg", "gg"));
        data.put("doc", list);
        SendData("post", path, data);
    }

    public void matchData() {

    }

    private void SendData(String method, String path, Map<String, List<TestEntity>> entities) throws IOException {
        Request request = new Request(method, path);
        if (!entities.isEmpty()) {
            String json = JacksonUtils.toJson(entities);
            HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
        }
        Response response = restClient.performRequest(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(result);
    }

    private String createPath(String index, String type, String id) {
        return "/" + index + "/" + type + "/" + id;
    }
}
