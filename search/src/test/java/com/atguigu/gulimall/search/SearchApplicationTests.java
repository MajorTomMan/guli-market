/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-12 12:19:15
 * @FilePath: /GuliMall/search/src/test/java/com/atguigu/gulimall/search/SearchApplicationTests.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.atguigu.gulimall.search.enitty.TestEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.util.ObjectBuilder;
import jakarta.json.Json;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class SearchApplicationTests {
    @Autowired
    RestClient restClient;
    @Autowired
    ElasticsearchClient esClient;
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
        TestEntity testEntity = new TestEntity("hh", "hh");
        IndexResponse response = esClient.index(
                i -> i.index("testentity")
                        .id("1").document(testEntity));
        log.info("Indexed with version " + response.version());
    }

    @Test
    public void deleteData() throws IOException {
        /*
         * String index = "tttttest";
         * String type = "_doc";
         * String id = "1";
         * String path = createPath(index, type, id);
         * SendData("delete", path, Collections.emptyMap());
         */
        DeleteResponse response = esClient.delete(
                d -> d.index("testentity").id("1"));
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
            System.out.println("找到ID:" + hit.id() + "分数:" + hit.score());
        }
    }

    @Test
    public void updateData() throws IOException {
        /*
         * String index = "tttttest";
         * String type = "_update";
         * String id = "1";
         * String path = createPath(index, type, id);
         * Map<String, List<TestEntity>> data = new HashMap<>();
         * List<TestEntity> list = new ArrayList<>();
         * list.add(new TestEntity("gg", "gg"));
         * data.put("doc", list);
         * SendData("post", path, data);
         */
        TestEntity testEntity = new TestEntity("TT", "TT");
        UpdateResponse<TestEntity> response = esClient.update(
                u -> u.index("testentity").id("1").doc(testEntity), TestEntity.class);
        log.info("Indexed with version " + response.version());
    }
    @Test
    public void matchData() throws ElasticsearchException, IOException {
        Query byName = MatchQuery.of(
                m -> m.field("name").query("hh"))._toQuery();
        Query byMaxPrice = RangeQuery.of(
                r -> r.field("sex").gte(JsonData.of("hh")))._toQuery();
        SearchResponse<TestEntity> response = esClient.search(
                s -> s.index("testentity").query(
                        q -> q.bool(
                                b -> b.must(byName).must(byMaxPrice))),
                TestEntity.class);
        List<Hit<TestEntity>> hits = response.hits().hits();
        for (Hit<TestEntity> hit : hits) {
            TestEntity entity = hit.source();
            System.out.println(entity);
            System.out.println("找到ID:" + hit.id() + "分数:" + hit.score());
        }
    }
    public void BulkData() throws ElasticsearchException, IOException{
        List<TestEntity> entities=new ArrayList<>();
        entities.add(new TestEntity("1","2"));
        entities.add(new TestEntity("3","4"));
        entities.add(new TestEntity("5","6"));
        List<BulkOperation> bulk = entities.stream().map(entity->{
          return new BulkOperation.Builder().create(
            c->c.index("testentity").id(entity.getName())
            .document(entity)
          ).build();
        }).collect(Collectors.toList());
        esClient.bulk(b->b.operations(bulk));
    } 
    private void SendData(String method, String path, Map<String, List<TestEntity>> entities) throws IOException {
        Request request = new Request(method, path);
        if (!entities.isEmpty()) {
            String json = new ObjectMapper().writeValueAsString(entities);
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
