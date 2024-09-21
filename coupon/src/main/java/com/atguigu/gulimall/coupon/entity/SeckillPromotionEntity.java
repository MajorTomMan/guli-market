/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-06-23 17:37:37
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-15 23:34:42
 * @FilePath: \Guli\coupon\src\main\java\com\atguigu\gulimall\coupon\entity\SeckillPromotionEntity.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀活动
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 11:00:44
 */
@Data
@TableName("sms_seckill_promotion")
public class SeckillPromotionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 活动标题
	 */
	private String title;
	/**
	 * 开始日期
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
	private Date startTime;
	/**
	 * 结束日期
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
	private Date endTime;
	/**
	 * 上下线状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
	private Date createTime;
	/**
	 * 创建人
	 */
	private Long userId;

}
