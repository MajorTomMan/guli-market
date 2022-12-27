/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-27 21:26:30
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 21:31:13
 * @FilePath: /common/home/master/project/gulimall/ware/src/main/java/com/atguigu/gulimall/ware/feign/ProductFeignService.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.ware.feign;

import com.atguigu.gulimall.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gateway")
public interface ProductFeignService {
    @RequestMapping("/api/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
