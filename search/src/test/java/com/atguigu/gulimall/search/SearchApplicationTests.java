/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 13:16:00
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-30 18:43:01
 * @FilePath: /common/home/master/project/GuliMall/search/src/test/java/com/atguigu/gulimall/search/SearchApplicationTests.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestClient restClient;
    @Test
    public void insertData() throws IOException{
        String index="tttttest";
        String type="_doc";
        String id="1";
        String path=createPath(index, type, id);
        String json = new ObjectMapper().writeValueAsString(new TestEntity("gg", "man"));
        SendData("post",json, path);
    }
    @Test
    public void deleteData() throws IOException{
        String index="tttttest";
        String type="_doc";
        String id="1";
        String path=createPath(index, type, id);
        SendData("delete", path);
    }
    @Test
    public void queryData() throws IOException{
        String index="tttttest";
        String type="_doc";
        String id="1";
        String path=createPath(index, type, id);
        SendData("get", path);
    }
    @Test
    public void updateData() throws IOException{
        String index="tttttest";
        String type="_update";
        String id="1";
        String path=createPath(index, type, id);
        Map<String,TestEntity> data=new HashMap<>();
        data.put("doc", new TestEntity("GGGGGGGG","gggggg"));
        String json = new ObjectMapper().writeValueAsString(data);
        System.out.println(json);
        SendData("post",json, path);
    }
    private void SendData(String method, String path) throws IOException {
        Request request=new Request(method,path);
        Response response = restClient.performRequest(request);
        HttpEntity entity = response.getEntity();
        String data = EntityUtils.toString(entity, "UTF8");
        System.out.println("data:"+data);
        System.out.println(entity.getContentType());
        System.out.println("status:"+response.getStatusLine().getStatusCode());
        System.out.println("hostname:"+response.getHost().getHostName());
    }
    private void SendData(String method,String data,String path) throws IOException{
        Request request=new Request(method,path);
        HttpEntity entity=new NStringEntity(data,ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        String jsondata = EntityUtils.toString(entity, "UTF8");
        System.out.println("data:"+jsondata);
        System.out.println(entity.getContentType());
        System.out.println("status:"+response.getStatusLine().getStatusCode());
        System.out.println("hostname:"+response.getHost().getHostName());
    }
    private String createPath(String index,String type,String id){
        return "/"+index+"/"+type+"/"+id;
        
    }
}
