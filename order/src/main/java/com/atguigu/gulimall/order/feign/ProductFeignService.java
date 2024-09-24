package com.atguigu.gulimall.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.gulimall.common.utils.R;

@FeignClient("product")
public interface ProductFeignService {

    /**
     * 根据skuId查询spu的信息
     * 
     * @param skuId
     * @return
     */
    @GetMapping(value = "/product/spuinfo/skuId/{skuId}")
    public R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);
}
