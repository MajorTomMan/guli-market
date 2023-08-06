/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:38:08
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-08-07 01:18:34
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/service/impl/ProductSaveServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gulimall.common.constant.ElasticConstant;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.search.service.ProductSaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Autowired
    ElasticsearchClient client;

    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModel) throws ElasticsearchException, IOException {
        // TODO Auto-generated method stub
        /*
         * 建立索引:product
         * 并建立映射关系
         */
        BulkRequest request = BulkRequest.of(b -> {
            List<BulkOperation> list = skuEsModel.stream().map(sku -> {
                BulkOperation bulkOperation = BulkOperation.of(bulk -> {
                    bulk.update(c -> {
                        c.index(ElasticConstant.PRODUCT_INDEX)
                                .id(sku.getSkuId().toString())
                                .action(a->a.doc(sku));
                        return c;
                    });
                    return bulk;
                });
                return bulkOperation;
            }).collect(Collectors.toList());
            b.operations(list);
            return b;
        });
        BulkResponse response = client.bulk(request);
        response.items().forEach(item -> {
            if (item.status() != 200) {
                log.error("{}批量处理失败", item.id());
                ErrorCause error = item.error();
                log.warn("因为什么引起:" + error.causedBy());
                log.warn("原因是:" + error.reason());
            } else {
                log.info("商品在ES中上架成功");
                log.info("状态:" + item.status());
                log.info("ID:" + item.id());
            }
        });
    }

    @Override
    public List<SkuEsModel> getSkuInfo() {
        List<SkuEsModel> skus = null;
        // TODO Auto-generated method stub
        SearchResponse<SkuEsModel> response;
        try {
            response = client.search(
                    r -> r.index(ElasticConstant.PRODUCT_INDEX), SkuEsModel.class);
            List<Hit<SkuEsModel>> hits = response.hits().hits();
            skus = hits.stream().map(sku -> {
                return sku.source();
            }).collect(Collectors.toList());
        } catch (ElasticsearchException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return skus;
    }
}
