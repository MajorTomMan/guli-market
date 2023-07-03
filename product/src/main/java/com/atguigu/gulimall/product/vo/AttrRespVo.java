package com.atguigu.gulimall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper=false)
@Data
public class AttrRespVo  extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
