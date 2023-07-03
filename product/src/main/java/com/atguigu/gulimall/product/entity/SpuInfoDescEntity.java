/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-12-26 21:32:56
 * @FilePath: /common/home/master/project/gulimall/product/src/main/java/com/atguigu/gulimall/product/entity/SpuInfoDescEntity.java
 * @Description: 
 * 
 * Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
 */
package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu信息介绍
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 10:52:58
 */
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = IdType.INPUT)
	private Long spuId;
	/**
	 * 商品介绍
	 */
	private String decript;

}
