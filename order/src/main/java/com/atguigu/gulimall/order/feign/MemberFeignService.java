package com.atguigu.gulimall.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.gulimall.order.vo.MemberAddressVo;

@FeignClient("member")
public interface MemberFeignService {
    @GetMapping("member/memberreceiveaddress/{memberId}/address")
    public List<MemberAddressVo> getAddress(Long memberId);
}
