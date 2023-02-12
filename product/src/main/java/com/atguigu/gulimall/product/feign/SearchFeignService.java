/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-12 14:16:02
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-12 14:18:55
 * @FilePath: /GuliMall/product/src/main/java/com/atguigu/gulimall/product/feign/SearchFeignService.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.feign;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> SkuEsModel);
}
