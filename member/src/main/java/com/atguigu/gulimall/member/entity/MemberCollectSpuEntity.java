package com.atguigu.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员收藏的商品
 * 
 * @author majorTom
 * @email flashnamesl@gmail.com
 * @date 2022-07-19 21:04:17
 */
@Data
@TableName("ums_member_collect_spu")
public class MemberCollectSpuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 会员id
	 */
	private Long memberId;
	/**
	 * spu_id
	 */
	private Long spuId;
	/**
	 * spu_name
	 */
	private String spuName;
	/**
	 * spu_img
	 */
	private String spuImg;
	/**
	 * create_time
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "Asia/Shanghai")
	private Date createTime;

}
