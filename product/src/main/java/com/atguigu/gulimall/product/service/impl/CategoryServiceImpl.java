package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Locale.Category;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>());

        return new PageUtils(page);
    }

    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> topLevelMenus = entities.stream().filter(
                categoryEntity-> categoryEntity.getParentCid() == 0
            ).map(
            (menu)->{
                menu.setChildren(getChildrens(menu, entities));
                return menu;
            }
        ).sorted(
            (menu1,menu2)->{
                return menu1.getSort()-menu2.getSort();
            }
        ).collect(Collectors.toList());
        return topLevelMenus;
    }
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(
            categoryEntity->{
                return categoryEntity.getParentCid()==root.getCatId();
            }
        ).map(
            categoryEntity->{
                categoryEntity.setChildren(getChildrens(categoryEntity, all));
                return categoryEntity;
            }
        ).sorted(
            (menu1,menu2)->{
                return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
            }
        ).collect(Collectors.toList());
        return children;
    }
}