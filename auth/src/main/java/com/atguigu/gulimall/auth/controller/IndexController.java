/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-20 22:11:08
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-20 22:11:19
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\controller\IndexController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class IndexController {
    @RequestMapping("/index")
    private String test() {
        return "hello";
    }
}
