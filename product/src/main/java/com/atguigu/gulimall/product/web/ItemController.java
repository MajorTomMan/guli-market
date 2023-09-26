package com.atguigu.gulimall.product.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.vo.SkuItemVo;

@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;

    /*
     * 展现SKU详情
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId) {
        SkuItemVo itemVo = skuInfoService.item(skuId);
        return "item";
    }
}
