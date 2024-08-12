/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-08-26 00:13:32
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-12 23:32:24
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\vo\AttrResponseVo.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class AttrResponseVo {

	/**
	 * 属性id
	 */
	@TableId(value = "attr_id", type = IdType.AUTO)
	private Long attrId;
	/**
	 * 属性名
	 */
	private String attrName;
	/**
	 * 是否需要检索[0-不需要，1-需要]
	 */
	private Integer searchType;
	/**
	 * 值类型[0-为单个值，1-可以选择多个值]
	 */
	private Integer valueType;
	/**
	 * 属性图标
	 */
	private String icon;
	/**
	 * 可选值列表[用逗号分隔]
	 */
	private String valueSelect;
	/**
	 * 属性类型[0-销售属性，1-基本属性
	 */
	private Integer attrType;
	/**
	 * 启用状态[0 - 禁用，1 - 启用]
	 */
	private Long enable;
	/**
	 * 所属分类
	 */
	private Long catelogId;
	/**
	 * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
	 */
	private Integer showDesc;

	private Long attrGroupId;
	private String catelogName;
	private String groupName;
	private Long[] catelogPath;
}
