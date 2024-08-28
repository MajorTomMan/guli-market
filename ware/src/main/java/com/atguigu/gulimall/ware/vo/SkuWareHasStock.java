package com.atguigu.gulimall.ware.vo;

import java.util.List;

import lombok.Data;

@Data
public class SkuWareHasStock {
    private Long skuId;
    private Integer num;
    private List<Long> wareId;
}