package com.atguigu.gulimall.seckill.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.gulimall.common.utils.R;

@FeignClient("coupon")
public interface CouponFeignService {
    @GetMapping("coupon/seckillsession/latest3DaySession")
    public R getLatest3Days();
}
