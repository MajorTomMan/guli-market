/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-27 17:40:52
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-27 17:42:02
 * @FilePath: /common/home/master/project/gulimall/ware/src/main/java/com/atguigu/gulimall/ware/vo/MergeVo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.ware.vo;

import java.util.List;

import lombok.Data;

@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;
}
