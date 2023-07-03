package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    CategoryBrandRelationService relationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key=(String)params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<BrandEntity>();
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key);

        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        // TODO Auto-generated method stub
        this.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            relationService.updateBrand(brand.getBrandId(),brand.getName());
        }
    }

}