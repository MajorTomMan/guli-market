package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class AttrVo extends AttrEntity {
    private Long attrGroupId;
}
