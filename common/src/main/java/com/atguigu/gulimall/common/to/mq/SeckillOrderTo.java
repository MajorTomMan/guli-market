/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-12-24 22:57:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-24 22:58:15
 * @FilePath: /common/src/main/java/com/atguigu/gulimall/common/to/mq/SeckillOrderTo.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class SeckillOrderTo {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 会员ID
     */
    private Long memberId;


}
