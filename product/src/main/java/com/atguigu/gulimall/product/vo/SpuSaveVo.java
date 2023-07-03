/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-23 21:15:32
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-01-30 14:51:12
 * @FilePath: /common/home/master/project/GuliMall/product/src/main/java/com/atguigu/gulimall/product/vo/SpuSaveVo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2020-05-31 11:3:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
public class SpuSaveVo {

  private String spuName;
  private String spuDescription;
  private Long catalogId;
  private Long brandId;
  private BigDecimal weight;
  private int publishStatus;
  private List<String> decript;
  private List<String> images;
  private Bounds bounds;
  private List<BaseAttrs> baseAttrs;
  private List<Skus> skus;


}
