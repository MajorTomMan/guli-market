/*
 * @Date: 2023-11-22 21:53:52
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-11-23 23:35:44
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\feign\MemberFeignService.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegisterVo;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.common.vo.SocialUserVo;

@FeignClient("member")
public interface MemberFeignService {
    @PostMapping("/member/member/regist")
    public R regist(@RequestBody UserRegisterVo vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth/login")
    public R oauthLogin(@RequestBody SocialUserVo vo);
}
