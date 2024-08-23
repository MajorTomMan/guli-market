package com.atguigu.gulimall.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.gulimall.common.utils.R;

@FeignClient("member")
public interface MemberFeignService {
    @RequestMapping("member/memberreceiveaddress/info/{id}")
    public R info(@PathVariable("id") Long id);
}
