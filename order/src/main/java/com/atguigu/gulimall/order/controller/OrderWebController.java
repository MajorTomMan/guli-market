package com.atguigu.gulimall.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
public class OrderWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String ToTrade(Model model) {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrderData", confirmVo);
        return "confirm";
    }

    /**
     * 下单,创建订单,验证令牌.验证价格,锁库存...
     * 成功 去往支付确认页
     * 失败 回到订单确认页重新确认订单信息
     * 
     * @param vo
     * @return 订单提交信息
     */
    @PostMapping("/submitOrder")
    public String SubmitOrder(@RequestBody OrderSubmitVo vo) {
        // TODO: process POST request
        SubmitOrderResponseVo response = orderService.submitOrder(vo);
        if (response.getCode() != 0) {
            log.error("订单提交失败:");
            return "redirect:http//order.gulimall.com/toTrade";
        } else {
            return "pay";
        }
    }

}
