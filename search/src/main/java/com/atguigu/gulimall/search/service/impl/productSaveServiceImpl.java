/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:38:08
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-11 22:48:53
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/service/impl/productSaveServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service.impl;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.search.service.ProductSaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@Service
public class productSaveServiceImpl implements ProductSaveService{
    @Autowired
    ElasticsearchClient client;
    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModel) {
        // TODO Auto-generated method stub
        
    }
    
}
