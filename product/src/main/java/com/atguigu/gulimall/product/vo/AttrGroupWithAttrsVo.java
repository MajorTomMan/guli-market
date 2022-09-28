/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-09-27 22:32:19
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-09-27 23:09:09
 * @FilePath: /common/home/master/project/gulimall/product/src/main/java/com/atguigu/gulimall/product/vo/AttrGroupWithAttrsVo.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.vo;

import java.util.List;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class AttrGroupWithAttrsVo {
	/**
	 * 分组id
	 */
	@TableId
	private Long attrGroupId;
	/**
	 * 组名
	 */
	private String attrGroupName;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 描述
	 */
	private String descript;
	/**
	 * 组图标
	 */
	private String icon;
	/**
	 * 所属分类id
	 */
	private Long catelogId;

	/* 
	 * 商品属性
	 */
	private List<AttrEntity> attrs;
}
