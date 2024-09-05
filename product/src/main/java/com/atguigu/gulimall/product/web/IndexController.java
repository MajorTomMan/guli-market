/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-13 23:22:09
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-08-25 00:08:23
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/web/IndexController.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.web;

import java.util.List;
import java.util.Map;

import com.atguigu.gulimall.common.vo.CartItemVo;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CartService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CartService cartService;
    @GetMapping({ "/", "/index.html" })
    public String indexPage(Model model) {
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories", categoryEntities);
        List<CartItemVo> cartItems = cartService.getCartItems();
        model.addAttribute("cart",cartItems);
        return "index";
    }

    /* 不要和Nginx中的静态匹配路径一样,不然nginx会默认你访问的是静态资源而报404 */
    /* 不要写/static/ */
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() throws JsonProcessingException, InterruptedException {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

}
