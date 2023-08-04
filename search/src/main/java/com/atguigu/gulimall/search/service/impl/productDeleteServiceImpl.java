/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-13 16:19:54
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-08-04 21:30:39
 * @FilePath: /common/home/master/project/GuliMall/search/src/main/java/com/atguigu/gulimall/search/service/impl/productDeleteServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.atguigu.gulimall.common.constant.ElasticConstant;
import com.atguigu.gulimall.search.service.ProductDeleteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class productDeleteServiceImpl implements ProductDeleteService {
    @Autowired
    ElasticsearchClient client;

    @Override
    public void deleteById(List<Long> skuIds) throws ElasticsearchException, IOException {
        // TODO Auto-generated method stub
        BulkRequest request = BulkRequest.of(b -> {
            List<BulkOperation> collect = skuIds.stream().map(id -> {
                BulkOperation bulkOperation = BulkOperation.of(bulk -> {
                    bulk.delete(d -> {
                        d.index(ElasticConstant.PRODUCT_INDEX).id(id.toString());
                        return d;
                    });
                    return bulk;
                });
                return bulkOperation;
            }).collect(Collectors.toList());
            b.operations(collect);
            return b;
        });
        BulkResponse response = client.bulk(request);
        for (BulkResponseItem item : response.items()) {
            log.warn(item.toString());
        }
    }

}
