package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 10:52:58
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
