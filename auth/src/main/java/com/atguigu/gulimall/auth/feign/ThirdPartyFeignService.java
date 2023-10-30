/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-30 23:43:17
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-30 23:52:00
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\feign\ThirdPartyFeignService.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.common.utils.R;
 
@FeignClient("third")
public interface ThirdPartyFeignService {
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone,@RequestParam("code") String code);
}
