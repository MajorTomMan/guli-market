package com.atguigu.gulimall.member.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * SocialMemberEntity
 */
@Data
public class SocialMemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String githubId;
    private String weiboId;
    private String qqId;
    private String weixinId;
}