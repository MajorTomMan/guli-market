/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-01-30 00:27:09
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-08 18:40:10
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/dao/AttrDao.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.dao;

import java.util.List;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品属性
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 10:52:58
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);
}
