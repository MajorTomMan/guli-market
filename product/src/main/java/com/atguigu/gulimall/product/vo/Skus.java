/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-23 21:15:37
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-23 21:18:48
 * @FilePath: /common/home/master/project/gulimall/product/src/main/java/com/atguigu/gulimall/product/vo/Skus.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
/** Copyright 2020 bejson.com */
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
public class Skus {

  private List<Attr> attr;
  private String skuName;
  private BigDecimal price;
  private String skuTitle;
  private String skuSubtitle;
  private List<Images> images;
  private List<String> descar;
  private int fullCount;
  private BigDecimal discount;
  private int countStatus;
  private BigDecimal fullPrice;
  private BigDecimal reducePrice;
  private int priceStatus;
  private List<MemberPrice> memberPrice;

}
