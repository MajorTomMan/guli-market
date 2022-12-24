/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-24 22:57:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-24 22:58:09
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/mq/StockDetailTo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to.mq;

import lombok.Data;


@Data
public class StockDetailTo {

    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 锁定状态
     */
    private Integer lockStatus;

}
