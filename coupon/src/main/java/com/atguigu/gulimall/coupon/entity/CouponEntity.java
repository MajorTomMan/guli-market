package com.atguigu.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券信息
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-21 11:00:44
 */
@Data
@TableName("sms_coupon")
public class CouponEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;
	/**
	 * 优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]
	 */
	private Integer couponType;
	/**
	 * 优惠券图片
	 */
	private String couponImg;
	/**
	 * 优惠卷名字
	 */
	private String couponName;
	/**
	 * 数量
	 */
	private Integer num;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	/**
	 * 每人限领张数
	 */
	private Integer perLimit;
	/**
	 * 使用门槛
	 */
	private BigDecimal minPoint;
	/**
	 * 开始时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
    private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
    private Date endTime;
	/**
	 * 使用类型[0->全场通用；1->指定分类；2->指定商品]
	 */
	private Integer useType;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 发行数量
	 */
	private Integer publishCount;
	/**
	 * 已使用数量
	 */
	private Integer useCount;
	/**
	 * 领取数量
	 */
	private Integer receiveCount;
	/**
	 * 可以领取的开始日期
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
    private Date enableStartTime;
	/**
	 * 可以领取的结束日期
	 */
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
    private Date enableEndTime;
	/**
	 * 优惠码
	 */
	private String code;
	/**
	 * 可以领取的会员等级[0->不限等级，其他-对应等级]
	 */
	private Integer memberLevel;
	/**
	 * 发布状态[0-未发布，1-已发布]
	 */
	private Integer publish;

}
