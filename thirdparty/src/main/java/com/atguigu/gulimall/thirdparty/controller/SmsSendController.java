/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-30 22:35:25
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-30 23:44:50
 * @FilePath: \Guli\thirdparty\src\main\java\com\atguigu\gulimall\thirdparty\controller\SMSSendController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.thirdparty.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.thirdparty.component.SMSComponent;
@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Autowired
    private SMSComponent smsComponent;
    /* 
     * 提供给别的服务进行调用
     */
    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone")String phone,@RequestParam("code")String code) throws InterruptedException, ExecutionException{
        smsComponent.sendSms(phone, code);
        return R.ok();
    }
}
