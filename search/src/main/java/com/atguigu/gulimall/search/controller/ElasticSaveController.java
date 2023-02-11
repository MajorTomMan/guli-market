/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 22:31:55
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-11 22:37:05
 * @FilePath: /GuliMall/search/src/main/java/com/atguigu/gulimall/search/controller/ElasticSaveController.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.search.controller;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.search.service.ProductSaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/search")
@RestController
public class ElasticSaveController {
    @Autowired 
    ProductSaveService productSaveService;
    // 上架商品
    @PostMapping("product")
    public R productStatusUp(@RequestBody List<SkuEsModel> SkuEsModel){
        return R.ok();
    }
}
