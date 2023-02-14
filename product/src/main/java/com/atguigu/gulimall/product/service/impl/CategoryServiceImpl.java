package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.atguigu.gulimall.product.vo.Catelog2Vo.Category3Vo;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryBrandRelationService relationService;

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
                categoryEntity -> categoryEntity.getParentCid() == 0).map(
                        (menu) -> {
                            menu.setChildren(getChildrens(menu, entities));
                            return menu;
                        })
                .sorted(
                        (menu1, menu2) -> {
                            return menu1.getSort() - menu2.getSort();
                        })
                .collect(Collectors.toList());
        return topLevelMenus;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(
                categoryEntity -> {
                    return categoryEntity.getParentCid() == root.getCatId();
                }).map(
                        categoryEntity -> {
                            categoryEntity.setChildren(getChildrens(categoryEntity, all));
                            return categoryEntity;
                        })
                .sorted(
                        (menu1, menu2) -> {
                            return (menu1.getSort() == null ? 0 : menu1.getSort())
                                    - (menu2.getSort() == null ? 0 : menu2.getSort());
                        })
                .collect(Collectors.toList());
        return children;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO Auto-generated method stub
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCateLogPath(Long cateLogId) {
        // TODO Auto-generated method stub
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(cateLogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        // TODO Auto-generated method stub
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            relationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        // TODO Auto-generated method stub
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // TODO Auto-generated method stub
        List<CategoryEntity> level1Categorys=getLevel1Categorys();

        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k->{
            return k.getCatId().toString();
        },v->{
            List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",v.getCatId()));
            List<Catelog2Vo> catelog2Vos=null;
            if(categoryEntities!=null){
                catelog2Vos=categoryEntities.stream().map(l2->
                {
                    Catelog2Vo catelog2Vo=new Catelog2Vo(v.getCatId().toString(),null,l2.getCatId().toString(),l2.getName());
                    List<CategoryEntity> level3Catelog = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",l2.getCatId()));
                    if(level3Catelog!=null){
                        List<Category3Vo> catalog3vo = level3Catelog.stream().map(l3->{
                            Category3Vo catelog3Vo= new Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3vo);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }
}