/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:31:55
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-13 16:31:48
 * @FilePath: /common/home/master/project/GuliMall/search/src/main/java/com/atguigu/gulimall/search/controller/ElasticSaveController.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.controller;

import java.util.List;

import com.atguigu.gulimall.common.exception.BizCodeEmum;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.search.service.ProductDeleteService;
import com.atguigu.gulimall.search.service.ProductSaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/search")
@RestController
public class ElasticOperationsController {
    @Autowired 
    ProductSaveService productSaveService;
    @Autowired
    ProductDeleteService productDeleteService;
    // 上架商品
    @PostMapping("/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> SkuEsModel){
        try {
            productSaveService.productStatusUp(SkuEsModel);
            return R.ok();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("错误原因是:{}",e.getMessage());
            log.error("ESController中商品上架错误");
            return R.error(BizCodeEmum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEmum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
    @PostMapping("/delete/product")
    public R productDeleteById(@RequestBody List<Long> skuIds){
        try {
            productDeleteService.deleteById(skuIds);
            return R.ok();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("错误原因是:{}",e.getMessage());
            return R.error(BizCodeEmum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEmum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
    @GetMapping("/info")
    public R SkuInfo(){
        List<SkuEsModel> skus=productSaveService.getSkuInfo();
        return R.ok().put("data", skus);
    }
}
