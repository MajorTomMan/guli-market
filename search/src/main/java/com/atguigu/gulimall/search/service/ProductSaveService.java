/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:35:08
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-12 14:14:35
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/service/ProductSaveService.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service;

import java.io.IOException;
import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;

public interface ProductSaveService {
    void productStatusUp(List<SkuEsModel> skuEsModel) throws ElasticsearchException, IOException;

    List<SkuEsModel> getSkuInfo();
}
