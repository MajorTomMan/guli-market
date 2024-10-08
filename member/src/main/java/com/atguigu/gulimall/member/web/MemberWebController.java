package com.atguigu.gulimall.member.web;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.member.feign.OrderFeignService;

@Controller
@RequestMapping
public class MemberWebController {
    @Autowired
    private OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", defaultValue = "1",required = false) Integer pageNum, Model model) {
        HashMap<String, Object> page = new HashMap<>();
        page.put("page", pageNum + "");
        R r = orderFeignService.listWithItem(page);
        model.addAttribute("orders", r);
        return "orderList";
    }
}
