/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-06-23 17:37:38
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-15 23:54:51
 * @FilePath: \Guli\product\src\main\java\com\atguigu\gulimall\product\entity\BrandEntity.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.entity;

import com.atguigu.gulimall.common.valid.AddGroup;
import com.atguigu.gulimall.common.valid.ListValue;
import com.atguigu.gulimall.common.valid.UpdateGroup;
import com.atguigu.gulimall.common.valid.UpdateStatusGroup;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 品牌
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 10:52:58
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改时必须指定品牌ID", groups = { UpdateGroup.class })
	@Null(message = "新建时不能指定品牌ID", groups = { AddGroup.class })
	@TableId(value = "brand_id",type = IdType.AUTO)
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "Logo地址不能为空", groups = { AddGroup.class })
	@URL(message = "Logo必须为合法url地址", groups = { AddGroup.class, UpdateGroup.class })
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "状态不能为空", groups = { AddGroup.class, UpdateStatusGroup.class })
	@ListValue(values = { 0, 1 }, groups = { AddGroup.class, UpdateStatusGroup.class })
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "首字母不能为空", groups = { AddGroup.class })
	@Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个英文字母", groups = { AddGroup.class, UpdateGroup.class })
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空", groups = { AddGroup.class })
	@Min(value = 0, message = "排序必须大于等于0", groups = { AddGroup.class, UpdateGroup.class })
	private Integer sort;

}
