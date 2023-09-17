/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-17 22:48:21
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/service/impl/SpuInfoServiceImpl.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.atguigu.gulimall.common.constant.ProductConstant;
import com.atguigu.gulimall.common.to.SkuHasStockVo;
import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.to.SpuBoundTo;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.to.es.SkuEsModel.Attrs;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.dao.SpuInfoDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;
import com.atguigu.gulimall.product.feign.CouponFeignService;
import com.atguigu.gulimall.product.feign.SearchFeignService;
import com.atguigu.gulimall.product.feign.WareFeignService;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.service.ProductAttrValueService;
import com.atguigu.gulimall.product.service.SkuImagesService;
import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.service.SkuSaleAttrValueService;
import com.atguigu.gulimall.product.service.SpuImagesService;
import com.atguigu.gulimall.product.service.SpuInfoDescService;
import com.atguigu.gulimall.product.service.SpuInfoService;
import com.atguigu.gulimall.product.vo.Attr;
import com.atguigu.gulimall.product.vo.BaseAttrs;
import com.atguigu.gulimall.product.vo.Bounds;
import com.atguigu.gulimall.product.vo.Images;
import com.atguigu.gulimall.product.vo.Skus;
import com.atguigu.gulimall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrService attrService;
    @Autowired
    ProductAttrValueService attrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>());

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        // TODO Auto-generated method stub
        /* 1.保存SPU基本信息: pms_spu_info */
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);
        /* 2.保存SPU的描述图片: pms_spu_info_desc */
        List<String> descript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", descript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);
        /* 3.保存SPU的图片集: pms_spu_images */
        List<String> images = vo.getImages();
        spuImagesService.saveImage(infoEntity.getId(), images);
        /* 4.保存SPU的规格参数: pms_product_attr_value */
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            valueEntity.setAttrName("");
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);
        /*
         * 5.保存当前SPU对应的所有SKU信息: gulimall_sms->sms_spu_bounds
         */
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存SPU积分信息失败");
        }
        /* 5.1 SKU的基本信息: PMS_SKU_INFO */
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    return StringUtils.isNotEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                /* 5.2 SKU的图片信息: PMS_SKU_IMAGES */
                // TODO 没有图片无需保存
                skuImagesService.saveBatch(imagesEntities);
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                /* 5.3 SKU的销售属性信息:PMS_SKU_SALE_ATTR_VALUE */
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                /*
                 * 5.4 SKU的优惠,满减等信息:GULIMALL_SMS->
                 * SMS_SKU_LODDER&SMS_SKU_FULL_REDUCTION&sms_member_price
                 */
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0
                        || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R rc = couponFeignService.saveSpuReduction(skuReductionTo);
                    if (rc.getCode() != 0) {
                        log.error("远程保存SKU积分信息失败");
                    }
                }
            });
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        // TODO Auto-generated method stub
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<SpuInfoEntity>();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catelog_Id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        // TODO Auto-generated method stub
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skuInfoEntities.stream().map(
                sku -> {
                    return sku.getSkuId();
                }).collect(Collectors.toList());
        List<SkuEsModel> models = skuInfoEntities.stream().map(
                sku -> {
                    SkuEsModel esModel = new SkuEsModel();
                    BeanUtils.copyProperties(sku, esModel);
                    // TODO 构造SKU检索属性
                    List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrListForSpu(sku.getSpuId());
                    List<Long> attrIds = baseAttrs.stream().map(attr -> {
                        return attr.getAttrId();
                    }).collect(Collectors.toList());
                    List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
                    Set<Long> idSet = new HashSet<>(searchAttrIds);
                    // TODO 发送远程调用查询库存
                    Map<Long, Boolean> stockMap = null;
                    try {
                        R<List<SkuHasStockVo>> r = wareFeignService.queryHasStock(skuIdList);
                        List<SkuHasStockVo> data = r.getData(new TypeReference<List<SkuHasStockVo>>() {
                        });
                        if (data != null) {
                            stockMap = new HashMap<>();
                            Map<Long, Boolean> finalstockMap = stockMap;
                            data.stream().forEach(item -> {
                                finalstockMap.put(item.getSkuId(), item.getHasStock());
                            });
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        log.error("远程查询库存信息失败 原因为:{}", e);
                    }
                    List<Attrs> attrsList = baseAttrs.stream().filter(
                            item -> {
                                return idSet.contains(item.getAttrId());
                            }).map(
                                    item -> {
                                        Attrs attrs = new Attrs();
                                        BeanUtils.copyProperties(item, attrs);
                                        return attrs;
                                    })
                            .collect(Collectors.toList());
                    esModel.setSkuPrice(sku.getPrice());
                    esModel.setSkuImg(sku.getSkuDefaultImg());
                    BrandEntity brand = brandService.getById(esModel.getBrandId());
                    CategoryEntity category = categoryService.getById(esModel.getCatelogId());
                    if (brand != null && category != null) {
                        esModel.setBrandName(brand.getName());
                        esModel.setBrandImg(brand.getLogo());
                        esModel.setCatelogId(category.getCatId());
                        esModel.setCatelogName(category.getName());
                        esModel.setHotScore(0L);
                    }
                    /* 设置库存信息 */
                    /* 若远程调用没有问题则插入数据 */
                    if (stockMap == null) {
                        esModel.setHasStock(true);
                    } else {
                        esModel.setHasStock(stockMap.get(sku.getSkuId()));
                    }
                    // 设置构造属性
                    esModel.setAttrs(attrsList);
                    return esModel;
                }).collect(Collectors.toList());
        // TODO 远程调用ES中的保存SKU接口,将数据保存到ES中
        R r = searchFeignService.productStatusUp(models);
        if (r.getCode() != 0) 
        {
            // 远程调用失败
            log.error("远程调用商品上架服务失败");
            log.error("原因是:"+r);
        } else {
            // 远程调用成功
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }
    }

    private void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }
}