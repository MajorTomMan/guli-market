package com.atguigu.gulimall.member.entity;

import java.lang.reflect.Member;

import com.atguigu.gulimall.common.userinfo.GithubUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class GithubMemberEntity extends GithubUserInfo{
    private String accessToken;
    private String tokenType;
}
