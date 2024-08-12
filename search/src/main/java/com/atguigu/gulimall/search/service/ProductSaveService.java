/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:35:08
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-12 22:58:13
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\service\ProductSaveService.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.service;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;


public interface ProductSaveService {
    void productStatusUp(List<SkuEsModel> skuEsModel) throws Exception;

    List<SkuEsModel> getSkuInfo();
}
