/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-04 21:03:20
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-12 23:45:42
 * @FilePath: \Guli\common\src\main\java\com\atguigu\gulimall\common\to\es\SkuEsModel.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to.es;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;
    @ToString
    @Data
    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }

}
