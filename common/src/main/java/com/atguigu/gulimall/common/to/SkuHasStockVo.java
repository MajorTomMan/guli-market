package com.atguigu.gulimall.common.to;

import lombok.Data;

/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2023-02-11 21:13:35
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2023-02-11 22:09:03
 * @FilePath: /GuliMall/common/src/main/java/com/atguigu/gulimall/common/to/SkuHasStockVo.java
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_name_email}, All Rights Reserved. 
 */
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
