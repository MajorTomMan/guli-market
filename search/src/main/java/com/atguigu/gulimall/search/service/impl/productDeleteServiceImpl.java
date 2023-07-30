/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-13 16:19:54
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-30 17:02:27
 * @FilePath: /common/home/master/project/GuliMall/search/src/main/java/com/atguigu/gulimall/search/service/impl/productDeleteServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gulimall.common.constant.ElasticConstant;
import com.atguigu.gulimall.search.service.ProductDeleteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class productDeleteServiceImpl implements ProductDeleteService{
    @Autowired
    ElasticsearchClient client;
    @Override
    public void deleteById(List<Long> skuIds) throws ElasticsearchException, IOException {
        // TODO Auto-generated method stub
        List<BulkOperation> deletes = skuIds.stream().map(
            skuId->{
                return new BulkOperation.Builder().delete(
                    d->d.index(ElasticConstant.PRODUCT_INDEX).id(skuId.toString())
                )
                .build();
            }
        ).collect(Collectors.toList());
        BulkResponse response = client.bulk(b->b.operations(deletes));
        log.info("响应结果:{}",response.toString());
        if (response.errors()) {
            log.error("批量处理失败");
            response.items().stream().forEach(item -> {
                log.error("{}操作失败", item.operationType());
                log.error("状态码是:{}",item.status());
                log.error("位置是索引:{}处,Id:{}处", item.index(), item.id());
                log.error("类名是:{}", item.getClass());
            });
        }
        else{
            log.info("商品在ES中删除成功");
        }
    }
    
}
