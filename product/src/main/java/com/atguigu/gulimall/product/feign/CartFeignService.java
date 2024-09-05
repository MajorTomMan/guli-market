package com.atguigu.gulimall.product.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.gulimall.common.vo.CartItemVo;

@FeignClient("cart")
public interface CartFeignService {
    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItemVo> getCurrentCartItems();
}
