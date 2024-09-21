/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-19 00:18:47
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/service/impl/SkuInfoServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.feign.SecKillFeignService;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.SkuImagesService;
import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.service.SkuSaleAttrValueService;
import com.atguigu.gulimall.product.service.SpuInfoDescService;
import com.atguigu.gulimall.product.vo.SeckillSkuVo;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;

@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    private SkuImagesService imagesService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private SecKillFeignService secKillFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>());

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {

        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<SkuInfoEntity>();
        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if (StringUtils.hasText(key)) {
            wrapper.and((w) -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        if (StringUtils.hasText(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        if (StringUtils.hasText(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catelog_Id", catelogId);
        }
        if (StringUtils.hasText(min)) {
            wrapper.ge("price", min);
        }
        if (StringUtils.hasText(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                BigDecimal bigDec = new BigDecimal("0");
                int result = bigDecimal.compareTo(bigDec);
                if (result == 1) {
                    wrapper.le("price", max);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Override
    public SkuItemVo item(Long skuId) throws InterruptedException, ExecutionException {

        /* 1.Sku基本信息获取 */
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, threadPoolExecutor);
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            /* 2.Sku的图片信息 */
            List<SkuImagesEntity> images = imagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, threadPoolExecutor);
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            /* 3.获取SPU的销售信息组合 */
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, threadPoolExecutor);
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            /* 4.获取SPU的介绍 */
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);
        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            /* 5.获取SPU的规格参数信息 */
            List<SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(),
                    res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVos);
        }, threadPoolExecutor);
        CompletableFuture<Void> secKillFuture = CompletableFuture.runAsync(() -> {
            // 3. 查询当前sku是否参与秒杀优惠
            R r = secKillFeignService.getSkuSecKillInfo(skuId);
            if (r.getCode() == 0) {
                SeckillSkuVo data = (SeckillSkuVo) r.getData(new TypeReference<SeckillSkuVo>() {
                });
                if (data != null) {
                    skuItemVo.setSeckillSkuVo(data);
                }
            }
        }, threadPoolExecutor);

        CompletableFuture.allOf(infoFuture, saleAttrFuture, descFuture, baseAttrFuture, imagesFuture, secKillFuture)
                .get();
        return skuItemVo;
    }
}