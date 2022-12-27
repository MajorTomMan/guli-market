/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 17:46:20
 * @FilePath: /common/home/master/project/gulimall/ware/src/main/java/com/atguigu/gulimall/ware/service/PurchaseService.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.vo.MergeVo;

import java.util.Map;

/**
 * 采购单
 *
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-19 21:08:09
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);
}

