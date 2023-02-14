/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-13 23:22:09
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-14 13:05:45
 * @FilePath: /GuliMall/product/src/main/java/com/atguigu/gulimall/product/web/IndexController.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.web;

import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;

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
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=categoryService.getLevel1Categorys();
        model.addAttribute("categories", categoryEntities);
        return "index";
    }
    @ResponseBody
    @GetMapping("/static/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson=categoryService.getCatalogJson();
        return catalogJson;
    }

}
