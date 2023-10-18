/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-09-26 21:14:51
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-18 00:05:07
 * @FilePath: \Guli\product\src\main\java\com\atguigu\gulimall\product\web\ItemController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.web;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.vo.SkuItemVo;

@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;

    /*
     * 展现SKU详情
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws InterruptedException, ExecutionException {
        System.out.println("准备查询" + skuId + "详情");
        SkuItemVo itemVo = skuInfoService.item(skuId);
        model.addAttribute("item", itemVo);
        return "item";
    }
}
