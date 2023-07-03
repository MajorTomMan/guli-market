/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-27 20:24:15
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 20:26:36
 * @FilePath: /common/home/master/project/gulimall/ware/src/main/java/com/atguigu/gulimall/ware/vo/PurchaseFinishVo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.ware.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PurchaseFinishVo {
    @NotNull
    private Long id;
    private List<ItemVo> items;
}
