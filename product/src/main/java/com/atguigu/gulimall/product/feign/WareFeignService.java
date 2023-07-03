/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-07 22:59:11
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-11 22:12:47
 * @FilePath: /GuliMall/product/src/main/java/com/atguigu/gulimall/product/feign/WareFeignService.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.feign;

import java.util.List;

import com.atguigu.gulimall.common.to.SkuHasStockVo;
import com.atguigu.gulimall.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R<List<SkuHasStockVo>> queryHasStock(@RequestBody List<Long> skuIds);
}
