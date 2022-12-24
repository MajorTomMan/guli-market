/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-24 22:57:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-24 22:58:57
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/SkuReductionTo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;



@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
