package com.atguigu.gulimall.search.service;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;

public interface ProductSaveService {
    void productStatusUp(List<SkuEsModel> skuEsModel);
}
