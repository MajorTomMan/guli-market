/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-03 13:40:46
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestClient restClient;

    private ElasticsearchClient init() {
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
        /* String index = "tttttest";
        String type = "_doc";
        String id = "1";
        String path = createPath(index, type, id);
        SendData("delete", path, Collections.emptyMap()); */
        ElasticsearchClient esClient = init();
        DeleteResponse response = esClient.delete(
            d->d.index("testentity").id("1")
        );
        log.info("Indexed with version " + response.version());
    }

    @Test
    public void queryData() throws IOException {
        /*
         * String index = "tttttest";
         * String type = "_doc";
         * String id = "1";
         * String path = createPath(index, type, id);
         * SendData("get", path, Collections.emptyMap());
         */
        ElasticsearchClient esClient = init();
        SearchResponse<TestEntity> response = esClient.search(
                s -> s.index("testentity").query(
                        q -> q.match(
                                t -> t.field("name").query("gg"))),
                TestEntity.class);
        TotalHits total = response.hits().total();
        Boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
        if (isExactResult) {
            log.info("总共有" + total.value() + "条结果");
        } else {
            log.info("总共有" + total.value() + "条以上结果");
        }
        List<Hit<TestEntity>> hits = response.hits().hits();
        for (Hit<TestEntity> hit : hits) {
            TestEntity entity = hit.source();
            System.out.println(entity);
            System.out.println("找到ID:"+hit.id()+"分数:"+hit.score());
        }
    }

    @Test
    public void updateData() throws IOException {
/*         String index = "tttttest";
        String type = "_update";
        String id = "1";
        String path = createPath(index, type, id);
        Map<String, List<TestEntity>> data = new HashMap<>();
        List<TestEntity> list = new ArrayList<>();
        list.add(new TestEntity("gg", "gg"));
        data.put("doc", list);
        SendData("post", path, data); */
        TestEntity testEntity = new TestEntity("TT", "TT");
        ElasticsearchClient esClient = init();
        UpdateResponse<TestEntity> response = esClient.update(
            u->u.index("testentity").id("1").doc(testEntity)
        , TestEntity.class);
        log.info("Indexed with version " + response.version());
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
