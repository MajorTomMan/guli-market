/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-24 22:57:05
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-24 22:58:48
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/SkuHasStockVo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to;

import lombok.Data;


@Data
public class SkuHasStockVo {

    private Long skuId;

    private Boolean hasStock;

}
