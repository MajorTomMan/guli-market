/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-24 22:57:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-24 22:57:58
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/mq/StockLockedTo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to.mq;

import java.io.Serializable;

import lombok.Data;

@Data
public class StockLockedTo {

    /** 库存工作单的id **/
    private Long id;

    /** 工作单详情的所有信息 **/
    private StockDetailTo detailTo;
}
