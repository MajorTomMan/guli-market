/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-04 21:03:20
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-04 21:10:20
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/es/SkuEsModel.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to.es;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

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
    private Long catelogId;
    private String brandName;
    private String brandImg;
    private String catelogName;
    private List<Attrs> attrs;
    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
