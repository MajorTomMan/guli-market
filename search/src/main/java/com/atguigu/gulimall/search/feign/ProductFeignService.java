/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-08-25 23:58:58
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-09-01 23:08:58
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\feign\ProductFeignService.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.gulimall.common.utils.R;

@FeignClient("product")
public interface ProductFeignService {
    @RequestMapping("product/attr/info/{attrId}")
    public R attrsInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("product/brand/infos")
    public R brandsInfo(@PathVariable("brandIds") List<Long> brandIds);

}
