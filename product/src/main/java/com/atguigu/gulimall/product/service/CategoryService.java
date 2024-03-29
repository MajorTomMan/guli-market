/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 00:27:09
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-19 18:31:02
 * @FilePath: /GuliMall/product/src/main/java/com/atguigu/gulimall/product/service/CategoryService.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 10:52:58
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
    public List<CategoryEntity> listWithTree();
    void removeMenuByIds(List<Long> asList);
    Long[] findCateLogPath(Long cateLogId);
    void updateCascade(CategoryEntity category);
    List<CategoryEntity> getLevel1Categorys();
    Map<String, List<Catelog2Vo>> getCatalogJson() throws JsonProcessingException, InterruptedException;
}

